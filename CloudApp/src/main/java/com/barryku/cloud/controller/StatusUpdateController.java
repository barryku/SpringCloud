package com.barryku.cloud.controller;

import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.TwitterTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

@Controller
public class StatusUpdateController {
	
	private static final String REQUEST_TOKEN = "user_request_token";
	private static final String ACCESS_TOKEN = "user_access_token";
	
	@Autowired
	@Qualifier("twitterOAuth")
	private OAuthService oauth;
	
	@Value("${twitter.authroizeUrl}")
	private String authorizeUrl;

	@Value("${twitter.apiKey}")
	private String apiKey;
	
	@Value("${twitter.apiSecret}")
	private String apiSecret;
	
	@Value("${twitter.myApiKey}")
	private String myApiKey;
	
	@Value("${twitter.myApiSecret}")
	private String myApiSecret;
	
	@Value("${twitter.myAccessToken}")
	private String myAccessToken;
	
	@Value("${twitter.myAccessTokenSecret}")
	private String myAccessTokenSecret;
	
	@RequestMapping(value={"/twitter"}, method=RequestMethod.GET )
	public String updateStatus(WebRequest request) {
		Token accessToken = (Token)request.getAttribute(ACCESS_TOKEN, WebRequest.SCOPE_SESSION);
		if (accessToken == null) {
			Token requestToken = oauth.getRequestToken();
			request.setAttribute(REQUEST_TOKEN, requestToken, WebRequest.SCOPE_SESSION);
			return "redirect:" + authorizeUrl + "?oauth_token=" + requestToken.getToken();
		} else {
			return "statusUpdate";
		}
	}
	
	@RequestMapping(value="/twitter/callback", method=RequestMethod.GET, params="oauth_token")
	public String authorizeCallback(@RequestParam(value = "oauth_verifier", defaultValue = "verifier") String verifier,
            WebRequest request) {
		Token requestToken = (Token)request.getAttribute(REQUEST_TOKEN, WebRequest.SCOPE_SESSION);
		Token accessToken = oauth.getAccessToken(requestToken, new Verifier(verifier));
		request.setAttribute(ACCESS_TOKEN, accessToken, WebRequest.SCOPE_SESSION);	
		return "redirect:../twitter";
	}

	@RequestMapping(value="/twitter", method=RequestMethod.POST )
	public String updateStatus(@RequestParam("message") String message, WebRequest request) {
		Token accessToken = (Token)request.getAttribute(ACCESS_TOKEN, WebRequest.SCOPE_SESSION);
		
		if (accessToken == null) {
			return "redirect:" + authorizeUrl;
		} else {
			TwitterTemplate template = new TwitterTemplate(apiKey, apiSecret, 
					accessToken.getToken(), accessToken.getSecret());
			template.updateStatus(message);
			return "statusUpdateComplete";
		}
	}
	
	@RequestMapping(value="/twitterme", method=RequestMethod.GET) 
	public String updateMyStatus() {
		return "statusUpdate";
	}
	
	@RequestMapping(value={"/twitterme"}, method=RequestMethod.POST )
	public String updateMyStatus(@RequestParam("message") String message, WebRequest request) {
			TwitterTemplate template = new TwitterTemplate(myApiKey, myApiSecret, 
					myAccessToken, myAccessTokenSecret);
			template.updateStatus(message);
			return "statusUpdateComplete";
	}
}
