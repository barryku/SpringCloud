package com.barryku.android.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class Copy2EmulatorActivity extends Activity {
	private static final String LOG_TAG = "copy2emulator";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String url = prefs.getString("url", null);
        TextView msg = (TextView) findViewById(R.id.user_message);
        if (url != null) {
        	doCopy(msg, url);
        } else {
        	msg.setText("Please click menu to set the URL you want to access in Prefereces.");
        }
    }
    
    private void doCopy(TextView msg, String url) {
    	DefaultHttpClient client = new DefaultHttpClient();
		String result = null;
		try {					
			Log.d(LOG_TAG, "accessing:" + url);	
			result =  client.execute(new HttpGet(url), new ResponseHandler<String>() {

				public String handleResponse(HttpResponse response)
						throws ClientProtocolException, IOException {
					HttpEntity entity = response.getEntity();
					String httpResult = null;
					try {
						httpResult = convertStreamToString(entity.getContent());
					} catch (IOException e) {
						Log.e(LOG_TAG, e.getMessage());
					}
					return httpResult;
				}						
				
			});
		} catch (ClientProtocolException e) {
			Log.e(LOG_TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(LOG_TAG, e.getMessage());
		}
		ClipboardManager mgr = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		mgr.setText(result);
		msg.setText("The following text was copied to clipboard,\n\n" + result);
    }
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_munu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
	    switch (item.getItemId()) {
		    case R.id.preference:
		    	Intent pref = new Intent(getBaseContext(), MyPreference.class);
		    	startActivity(pref);
		    	break;
		    case R.id.reload:
		    	doCopy();
		    	break;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
    private String convertStreamToString(InputStream is) {
		 
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return line;
    }
}