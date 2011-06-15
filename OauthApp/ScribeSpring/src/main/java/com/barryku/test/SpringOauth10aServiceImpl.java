package com.barryku.test;

import java.util.Map;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuth10aServiceImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class SpringOauth10aServiceImpl extends OAuth10aServiceImpl {
	private OAuthConfig config;
	private DefaultApi10a api;
	  
	public SpringOauth10aServiceImpl(DefaultApi10a api, OAuthConfig config) {
		super(api, config);
		this.api = api;
		this.config = config;
	}
	
	public Token getAccessToken(Token requestToken, Verifier verifier) {
		OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
		request.addOAuthParameter(OAuthConstants.TOKEN, requestToken.getToken());
		request.addOAuthParameter(OAuthConstants.VERIFIER, verifier.getValue());
		addOAuthParams(request, requestToken);
		addSignature(request);
		RestTemplate rest = RestUtil.getRestTemplate();
		ResponseEntity<String> response = rest.exchange(request.getUrl(), HttpMethod.POST, toHttpEntity(request), String.class);
		return api.getAccessTokenExtractor().extract(response.getBody());
	}


	public Token getRequestToken() {
		OAuthRequest request = new OAuthRequest(api.getRequestTokenVerb(), api.getRequestTokenEndpoint());
	    request.addOAuthParameter(OAuthConstants.CALLBACK, config.getCallback());
	    addOAuthParams(request, OAuthConstants.EMPTY_TOKEN);
	    addSignature(request);
	    RestTemplate rest = RestUtil.getRestTemplate();
	    ResponseEntity<String> response = rest.exchange(request.getUrl(), HttpMethod.POST, toHttpEntity(request),String.class);
	    return api.getRequestTokenExtractor().extract(response.getBody());
	}
	
	public HttpEntity<String> toHttpEntity(OAuthRequest request) {
		HttpHeaders headers = new HttpHeaders();
		for (Map.Entry<String, String> entry: request.getHeaders().entrySet()) {
			headers.add(entry.getKey(), entry.getValue());
		}
		return new HttpEntity<String>(headers);
	}
	
	private void addOAuthParams(OAuthRequest request, Token token)
	  {
	    request.addOAuthParameter(OAuthConstants.TIMESTAMP, api.getTimestampService().getTimestampInSeconds());
	    request.addOAuthParameter(OAuthConstants.NONCE, api.getTimestampService().getNonce());
	    request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, config.getApiKey());
	    request.addOAuthParameter(OAuthConstants.SIGN_METHOD, api.getSignatureService().getSignatureMethod());
	    request.addOAuthParameter(OAuthConstants.VERSION, getVersion());
	    if(config.hasScope()) request.addOAuthParameter(OAuthConstants.SCOPE, config.getScope());
	    request.addOAuthParameter(OAuthConstants.SIGNATURE, getSignature(request, token));
	  }
	
	private void addSignature(OAuthRequest request)
	  {
	    switch (config.getSignatureType())
	    {
	      case Header:
	        String oauthHeader = api.getHeaderExtractor().extract(request);
	        request.addHeader(OAuthConstants.HEADER, oauthHeader);
	        break;
	      case QueryString:
	        for (Map.Entry<String, String> entry : request.getOauthParameters().entrySet())
	        {
	          request.addQuerystringParameter(entry.getKey(), entry.getValue());
	        }
	        break;
	    }
	  }
	
	 private String getSignature(OAuthRequest request, Token token)
	  {
	    String baseString = api.getBaseStringExtractor().extract(request);
	    return api.getSignatureService().getSignature(baseString, config.getApiSecret(), token.getSecret());
	  }
}
