package com.barryku.test;

import org.scribe.builder.api.YahooApi;
import org.scribe.model.OAuthConfig;
import org.scribe.oauth.OAuthService;

public class SpringYahooApi extends YahooApi {
	 public OAuthService createService(OAuthConfig config)
	  {
	    return new SpringOauth10aServiceImpl(this, config);
	  }
}
