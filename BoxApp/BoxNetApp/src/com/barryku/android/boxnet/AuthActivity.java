/*
 * Copyright 2011 Barry Ku.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.barryku.android.boxnet;

import org.springframework.web.client.RestTemplate;

import com.barryku.android.boxnet.model.RequestToken;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import static com.barryku.android.boxnet.Constants.*;

public class AuthActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        	
        Intent intent = getIntent();        
        if (AUTH_INTENT_SCHEME.equals(intent.getScheme())) {
        	Uri uri = intent.getData();
        	Log.d(LOG_TAG, "processing auth callback: " + uri);
        	String authToken = uri.getQueryParameter("auth_token");
        	if (authToken != null) {        		
        		SharedPreferences.Editor editor = prefs.edit();
            	editor.putString(AUTH_TOKEN_KEY, authToken);
            	editor.commit();
        	}
        }
        Button login = (Button) findViewById(R.id.btnLogin);
        login.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				String requestToken = getRequestToken();
				Log.d(LOG_TAG, requestToken);
				Intent authIntent = new Intent("android.intent.action.VIEW",
			            Uri.parse(getString(R.string.auth_uri) + requestToken));
			    startActivity(authIntent);				
			}
			
			private String getRequestToken() {
				RestTemplate restTemplate = RestUtil.getRestTemplate();
				RequestToken resp = restTemplate.getForObject(getString(R.string.request_uri), RequestToken.class, getString(R.string.api_key));
				return resp.getRequestToken();
			}
		});
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString(AUTH_TOKEN_KEY, "").length() > 0) {
        	Log.d(LOG_TAG, "found authToken:" + prefs.getString(AUTH_TOKEN_KEY, ""));
        	forwardToBrowse();
        }
    }
    
    
    private void forwardToBrowse() {
    	Intent browseIntent = new Intent(this, BrowseActivity.class);
    	startActivity(browseIntent);
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
    	super.onNewIntent(intent);
    	Log.d(LOG_TAG, "url: " + intent.getData());
    }
}