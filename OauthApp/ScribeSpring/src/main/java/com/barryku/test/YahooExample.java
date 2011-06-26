package com.barryku.test;

import java.util.Scanner;

import org.scribe.builder.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

public class YahooExample 
{
  //private static final String PROTECTED_RESOURCE_URL = "http://social.yahooapis.com/v1/user/%s/profile/status?format=json";
	private static final String API_KEY = "dj0yJmk9TDMyc0prOFlFb3NuJmQ9WVdrOWVXNXFVa3hpTnpZbWNHbzlNak00TmpnME1EWXkmcz1jb25zdW1lcnNlY3JldCZ4PWVl";
	private static final String API_SECRET = "e8fb247893d0de0a72842be2ded4a57d58520ee6";
	private static final String PROTECTED_RESOURCE_URL = "http://social.yahooapis.com/v1/user/%s/contacts;name.contains=michael?format=json";
  public static void main(String[] args)
  {
	  
    OAuthService service = new ServiceBuilder()
                                .provider(SpringYahooApi.class)
                                .apiKey(API_KEY)
                                .apiSecret(API_SECRET)
                                .callback("http://oauth.cloudfoundry.com/auth?callback")
                                .build();    
    
    Scanner in = new Scanner(System.in);

    System.out.println("=== Yahoo's OAuth Workflow ===");
    System.out.println();

    // Obtain the Request Token
    System.out.println("Fetching the Request Token...");
    Token requestToken = service.getRequestToken();
    System.out.println("Got the Request Token!");
    System.out.println();

    System.out.println("Now go and authorize Scribe here:");
    System.out.println(service.getAuthorizationUrl(requestToken));
    System.out.println("And paste the verifier here");
    System.out.print(">>");
    Verifier verifier = new Verifier(in.nextLine());
    System.out.println();

    // Trade the Request Token and Verfier for the Access Token
    System.out.println("Trading the Request Token for an Access Token...");
    Token accessToken = service.getAccessToken(requestToken, verifier);
    System.out.println("Got the Access Token!");
    System.out.println("(if your curious it looks like this: " + accessToken + " )");
    System.out.println();

    // Now let's go and ask for a protected resource!
    System.out.println("Now we're going to access a protected resource...");
    OAuthRequest request = new OAuthRequest(Verb.GET, String.format(PROTECTED_RESOURCE_URL, getYahooGuid(accessToken.getRawResponse())));
    service.signRequest(accessToken, request);
    Response response = request.send();
    System.out.println("Got it! Lets see what we found...");
    System.out.println();
    System.out.println(response.getCode());
    System.out.println(response.getBody());

    System.out.println();
    System.out.println("Thats it man! Go and build something awesome with Scribe! :)");

  }
  
  private static final String YAHOO_GUID = "xoauth_yahoo_guid";
  private static final int GUID_LENGTH = 26;
  private static String getYahooGuid(String response) {
      String yahoo_guid = null;
      int yahoo_guid_location = response.indexOf(YAHOO_GUID);
      if ( yahoo_guid_location > 0) {
          yahoo_guid = response.substring(yahoo_guid_location + YAHOO_GUID.length() + 1,
                  yahoo_guid_location + YAHOO_GUID.length() + GUID_LENGTH + 1);
      }
      return yahoo_guid;
  }
}
