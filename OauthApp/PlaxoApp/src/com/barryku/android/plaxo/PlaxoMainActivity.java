package com.barryku.android.plaxo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.PlaxoApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PlaxoMainActivity extends Activity {
	private static final String RESOURCE_URL = 
		"http://www.plaxo.com/pdata/contacts/@me/@all?filterBy=displayName&filterOp=contains&filterValue=%s";
	private static final String PREF_ACCESS_TOKEN = "ACCESS_TOKEN";
	private static final String PREF_REQUEST_TOKEN = "REQUEST_TOKEN";
	private static final String LOG_TAG = "mydebug";
	private String oauthScheme;
	private OAuthService oauth;
	private SharedPreferences prefs;
	private SpringOauthService springOauth;
	private Token accessToken;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oauthScheme = getString(R.string.oauth_scheme);
        
        setContentView(R.layout.main);
        oauth = new ServiceBuilder()
        .provider(PlaxoApi.class)
        .apiKey("anonymous")
        .apiSecret("")
        .callback(oauthScheme + "://auth?callback")
        .build();
    	/**
    	oauth = new ServiceBuilder()
        .provider(YahooApi.class)
        .apiKey("dj0yJmk9TXZDWVpNVVdGaVFmJmQ9WVdrOWMweHZXbkZLTkhVbWNHbzlNVEl5TWprd05qUTJNZy0tJnM9Y29uc3VtZXJzZWNyZXQmeD0wMw--")
        .apiSecret("262be559f92a2be20c4c039419018f2b48cdfce9")
        .build();
    	**/
        springOauth = new SpringOauthService(oauth);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        //handling oauth callback
        Intent intent = getIntent();
        
        if (oauthScheme.equals(intent.getScheme())) {
        	Uri uri = intent.getData();
        	String verifier = uri.getQueryParameter("oauth_verifier");
        	if (verifier != null) {
        		Token requestToken = fromString(prefs.getString(PREF_REQUEST_TOKEN, null));
        		accessToken = springOauth.getAccessToken(requestToken, new Verifier(verifier));
        		SharedPreferences.Editor editor = prefs.edit();
        		editor.putString(PREF_ACCESS_TOKEN, convertToString(accessToken));
        		editor.commit();
        	}
        }    
        
        Button doSearch = (Button) findViewById(R.id.doSerarch);
        doSearch.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				EditText searchTxt = (EditText) findViewById(R.id.searchText);
				OAuthRequest request = new OAuthRequest(Verb.GET,
			            String.format(RESOURCE_URL,  searchTxt.getText()));
				springOauth.signRequest(accessToken, request);
				RestTemplate rest = RestUtil.getRestTemplate();
			    ResponseEntity<String> response = rest.exchange(request.getUrl(), HttpMethod.GET, springOauth.toHttpEntity(request),String.class);
			    TextView result = (TextView) findViewById(R.id.result);
			    result.setText(response.getBody());
			}
		});
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	String accessTokenString = prefs.getString(PREF_ACCESS_TOKEN, null);  
        
        if (accessTokenString == null) {
        	setSearchBarVisibility(false);
        	
        } else {
        	setSearchBarVisibility(true);
        	accessToken = fromString(prefs.getString(PREF_ACCESS_TOKEN, null));
        	Log.d(LOG_TAG, "access token: " + accessToken);
        }
    }
    
    private void setSearchBarVisibility(boolean visible){
    	TextView result = (TextView) findViewById(R.id.result);
        RelativeLayout searchBar = (RelativeLayout) findViewById(R.id.searchBar);
        searchBar.setVisibility(visible?View.VISIBLE:View.INVISIBLE);
    	result.setText(visible ? "" : getString(R.string.not_authorized));  
    }
    
    private void setAuthMenuVisibility(boolean visible, Menu menu) {
    	MenuItem authorize = menu.findItem(R.id.authorize);
    	if (authorize != null) {
	        MenuItem logout = menu.findItem(R.id.logout);
	        authorize.setVisible(visible);
	     	logout.setVisible(!visible);
    	}
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	if (accessToken == null) {
	    	setAuthMenuVisibility(true, menu);
	    } else {
	    	setAuthMenuVisibility(false, menu);
	    }
    	return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_menu, menu);	    
	    return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    case R.id.authorize:	    	
	    	Token requestToken = springOauth.getRequestToken();

	    	Log.d(LOG_TAG, "token: " + requestToken);
	    	SharedPreferences.Editor editor = prefs.edit();
	    	editor.putString(PREF_REQUEST_TOKEN, convertToString(requestToken));
	    	editor.commit();
	    	Intent authIntent = new Intent("android.intent.action.VIEW",
		            Uri.parse(oauth.getAuthorizationUrl(requestToken)));
		    startActivity(authIntent);	
	    	break;	
	    case R.id.logout:
	    	SharedPreferences.Editor editor2 = prefs.edit();
	    	editor2.remove(PREF_ACCESS_TOKEN);
	    	editor2.remove(PREF_REQUEST_TOKEN);
	    	editor2.commit();
	    	accessToken = null;
	    	setSearchBarVisibility(false);
	    	break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private String convertToString(Token token) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
	        ObjectOutputStream oos = new ObjectOutputStream( baos );
	        oos.writeObject( token );
	        oos.close();
		} catch (IOException ioe) {
			Log.d(LOG_TAG, "conversion failed: " + ioe.getMessage());
		}
        return  Base64.encodeToString( baos.toByteArray(), Base64.DEFAULT );
	}
	
	private Token fromString(String str) {
		byte [] data = Base64.decode(str, Base64.DEFAULT );
        ObjectInputStream ois;
        Object o = null;
		try {
			ois = new ObjectInputStream(new ByteArrayInputStream(  data ) );			
	        o  = ois.readObject();
	        ois.close();
		} catch (Exception e) {
			Log.d(LOG_TAG, "conversion failed: " + e.getMessage());
		}
        return (Token) o;
	}
} 