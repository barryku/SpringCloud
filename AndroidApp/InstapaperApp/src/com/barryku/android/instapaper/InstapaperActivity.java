package com.barryku.android.instapaper;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class InstapaperActivity extends Activity {
    public static final String LOG_TAG = "instapaper_app";
    public static final Map<String, String> RESPONSE_CODE;
    public static final String SUCCESSFUL_CODE = "201";
    private UrlLinkDataManager db;
    private boolean isInvokedFromShareVia;
    
    static {
    	RESPONSE_CODE = new HashMap<String, String>();
    	RESPONSE_CODE.put(SUCCESSFUL_CODE, "URL has been successfully added to Instapaper.");
    	RESPONSE_CODE.put("400", "Bad request or exceeded the rate limit. Probably missing a required parameter, such as url.");
    	RESPONSE_CODE.put("403", "Invalid username or password.");
    	RESPONSE_CODE.put("500", "The service encountered an error. Please try again later.");
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.main);
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String userName = prefs.getString("userName", null);
        db = new UrlLinkDataManager(this);
        
		Intent intent = getIntent();
		TextView msg = (TextView) findViewById(R.id.user_message);
		ListView lview = (ListView) findViewById(R.id.urlListView);
		isInvokedFromShareVia = (intent.getExtras() != null);
		if (userName != null) {
			if (isInvokedFromShareVia) {
		    	msg.setText(R.string.in_progress);
		    	lview.setVisibility(View.INVISIBLE);
				final String uri = (String) intent.getExtras().get(Intent.EXTRA_TEXT);
				Log.d(LOG_TAG, "uri:" + uri);
				ConnectivityManager cMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
				if (!cMgr.getActiveNetworkInfo().isAvailable()) {
					db.addLink(uri);
				} else {			
					doPost(uri);
				}
			} 
		} else {			
			msg.setText(R.string.no_credential);
		}
    }       
    
    public void doPostDone() {
    	if (isInvokedFromShareVia)
    		finish();
    }
    
    private void doPost(String uri) {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String userName = prefs.getString("userName", null);
        final String password = prefs.getString("password", null);    	
		AsyncTask<String, Void, String> poster = new LinkPoster(this);
		poster.execute(getString(R.string.instapaper_url), userName,password,uri);
    }
    
    public void saveLink(String link) {
    	db.addLink(link);
    	final ListView lview = (ListView) findViewById(R.id.urlListView);
    	refreshListView(lview);
		
    }
    
    private void refreshListView(final ListView lview) {
    	TextView msg = (TextView) findViewById(R.id.user_message);
    	msg.setText("Tap on any saved URL to resend it to Instapaper.");
    	lview.setAdapter(new ArrayAdapter<UrlLink>(this, android.R.layout.simple_list_item_1, db.getLinks()));
    	lview.invalidate();  
    	lview.setVisibility(View.VISIBLE);
    	lview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				UrlLink link = (UrlLink) lview.getItemAtPosition(position);	
				doPost(link.getLink());
				db.removeLink(link);	
				refreshListView(lview);
			}
        	
		});
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	Log.d(LOG_TAG, "onResume....");
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String userName = prefs.getString("userName", null);
        if (! (userName == null || isInvokedFromShareVia)) {
        	ListView lview = (ListView) findViewById(R.id.urlListView);
        	refreshListView(lview);
        }
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
		    	Intent pref = new Intent(getBaseContext(), InstapaperPreference.class);
		    	startActivity(pref);
		    	break;
	    }
	    return super.onOptionsItemSelected(item);
	}
  
}