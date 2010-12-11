package com.barryku.android.plaxo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PlaxoSearch extends Activity {
	private final PlaxoSearch parent = this;
	private String userName;
	private String password;
	
	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}
	
	public String getStringMsg(int resId) {
		return getString(resId);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.plaxo_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.preference:
	    	Intent pref = new Intent(getBaseContext(), PlaxoPreference.class);
	    	startActivity(pref);
	    	break;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	private static float TEXT_NORMAL_SIZE = 14;
	private static float TEXT_ZOOM_SIZE = 19;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final TextView result = (TextView) findViewById(R.id.result);
        	
        Button searchBtn = (Button) findViewById(R.id.searchBtn);        
        searchBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				performSearch();				
			}
		});
        
        
        Button zoomBtn = (Button) findViewById(R.id.zoomBtn);        
        zoomBtn.setOnClickListener(new OnClickListener() {
        	float currentSize = TEXT_NORMAL_SIZE;
			@Override
			public void onClick(View v) {
				if (currentSize == TEXT_NORMAL_SIZE) {
					result.setTextSize(TypedValue.COMPLEX_UNIT_DIP, TEXT_ZOOM_SIZE);
					currentSize = TEXT_ZOOM_SIZE;
				} else {
					result.setTextSize(TypedValue.COMPLEX_UNIT_DIP, TEXT_NORMAL_SIZE);
					currentSize = TEXT_NORMAL_SIZE;
				}
			}
		});
        
        EditText searchText = (EditText) findViewById(R.id.searchText);
        searchText.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                  performSearch();
                  return true;
                }
                return false;
			}
        });  
        
    }
    
    private void performSearch() {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	userName = prefs.getString("userName", getString(R.string.userName));
    	password = prefs.getString("password", getString(R.string.password));
    	TextView result = (TextView) findViewById(R.id.result);
    	result.setText("\nsearching, please wait...");
    	EditText searchText = (EditText) findViewById(R.id.searchText);
		ContactSearcher searcher = new ContactSearcher(parent);
		searcher.execute(searchText.getText().toString());
    }
}