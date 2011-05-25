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

import static com.barryku.android.boxnet.Constants.*;

import org.springframework.web.client.RestTemplate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;

import com.barryku.android.boxnet.model.AccountTree;
import com.barryku.android.boxnet.model.FileItem;
import com.barryku.android.boxnet.model.FolderItem;
import com.barryku.android.boxnet.model.RestResponse;

public class BrowseActivity extends Activity {
	
	private String restUri;
	private String apiKey;
	private String authToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.browse);
		restUri = getString(R.string.rest_uri);
		apiKey = getString(R.string.api_key);	
		
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String folderId = prefs.getString(LAST_VIEWED_FOLDER, "0");
		authToken = prefs.getString(AUTH_TOKEN_KEY, "");
		
		Uri uri = this.getIntent().getData();
		if (uri != null) {
			folderId = uri.getPath().substring(1);
		}
		Log.d(LOG_TAG, "loading folder from onCreate()");
		loadFolder(folderId);
		
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		Uri uri = intent.getData();
		
		Log.d(LOG_TAG, "loading folder from onNewIntent() from:" + uri);
		loadFolder(uri.getPath().substring(1)); //remove leading slash
	}
	
	private void loadFolder(String folderId) {		
		String browseUri = BOX_INTENT_SCHEME + "://" + BOX_INTENT_BROWSE + "/";
		String uploadUri = BOX_INTENT_SCHEME + "://" + BOX_INTENT_UPLOAD + "/";;
		String downloadUri = DOWNLOAD_URI + authToken + "/";
		String homeUrl = browseUri + 0;
			
		if (folderId == null || folderId.equals(""))
			folderId = "0";		
		
		Log.d(LOG_TAG, "loading folder:" + folderId);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(LAST_VIEWED_FOLDER, folderId);
		editor.commit();
		
		RestTemplate rest = RestUtil.getRestTemplate();
		String requestUri = restUri + "get_account_tree&params[]=nozip&params[]=onelevel&folder_id={folderId}";
		AccountTree response = rest.getForObject(requestUri, AccountTree.class, apiKey, authToken, folderId);
		
		//authToken expires at certain interval which will require authentication again
		if (!STATUS_LISTING_OK.equals(response.getStatus())) {
			Log.d(LOG_TAG, "authToken no longer works");
			clearPreferences(prefs);
		}
		
		StringBuilder sb = new StringBuilder();
		if (response == null) {
			sb.append("no data found");
		} else {
			if (!"0".equals(folderId)) {
				sb.append("<a href=\"").append(homeUrl).append("\">").append
				("<img src=\"").append(IMG_HOME_URL).append("\"> ").append("return to home").append("</a><br>");
			}
			
			if (response.getFolders() != null) {
				for (FolderItem folder: response.getFolders()) {
					sb.append("<a href=\"").append(browseUri).append(folder.getId()).append("\">").append
							("<img src=\"").append(IMG_FOLDER_URL).append("\"> ").append(folder.getName()).append("</a><br>");
				}
			}
			
			if (response.getFiles() != null) {
				for (FileItem file: response.getFiles()) {
					sb.append("<a href=\"").append(downloadUri).append(file.getId()).append("\">")
						.append(file.getName()).append("</a><br/>");
				}
			}
			
			sb.append("<br/><br/><a href=\"").append(uploadUri).append(folderId).append("\">")
			.append("upload file ").append("<img src=\"").append(IMG_UPLOAD_URL).append("\"></a>");
			
			final WebView detailResult = (WebView) findViewById(R.id.browseView);
	        detailResult.getSettings().setBuiltInZoomControls(true);
	        detailResult.loadDataWithBaseURL("fake://notused", "<html><body>" + sb.toString() + 
	        	"</body></html>", "text/html", "UTF-8", "");
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.browse_option_menu, menu);
	    return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    case R.id.logout:
	    	RestTemplate rest = RestUtil.getRestTemplate();
	    	RestResponse response = rest.getForObject(getString(R.string.logout_uri), RestResponse.class, apiKey, authToken);
	    	Log.d(LOG_TAG, "log out with status:" + response.getStatus());
	    	clearPreferences(PreferenceManager.getDefaultSharedPreferences(this));
	    	break;	

		}
		return super.onOptionsItemSelected(item);
	}

	private void clearPreferences(SharedPreferences prefs) {
		SharedPreferences.Editor editor = prefs.edit();
		editor = prefs.edit();
		editor.clear();
		editor.commit();
		Intent authIntent = new Intent(this, AuthActivity.class);
    	startActivity(authIntent);
	}
}
