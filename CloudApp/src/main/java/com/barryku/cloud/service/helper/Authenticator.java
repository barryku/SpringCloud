package com.barryku.cloud.service.helper;

import java.io.IOException;

import org.apache.http.HttpRequest;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

public class Authenticator {
	private String token;
	private String tokenSecret;	
    private OAuthConsumer consumer;	
    private OAuthProvider provider;
    
    public void setConsumer(OAuthConsumer cons) {
    	consumer = cons;
    }
    
    public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTokenSeceret() {
		return tokenSecret;
	}

	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}

	public void setProvider(OAuthProvider prov) {
    	provider = prov;
    }	
    
    public void sign(HttpRequest request) throws OAuthMessageSignerException, 
    	OAuthCommunicationException, OAuthExpectationFailedException {
        // create a consumer object and configure it with the access
        // token and token secret obtained from the service provider
        OAuthConsumer cons = new CommonsHttpOAuthConsumer(token, tokenSecret);
        cons.setTokenWithSecret(consumer.getToken(), consumer.getTokenSecret());

        // sign the request
		cons.sign(request);
    }
    
    public String retrieveRequestToken(String callback) throws IOException, OAuthException, OAuthCommunicationException 
    {
        String url = provider.retrieveRequestToken(consumer, callback);
        return url;
    }
    
    public void retrieveAccessToken(String verifier) throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException
    {
        provider.retrieveAccessToken(consumer, verifier);
    }
}
