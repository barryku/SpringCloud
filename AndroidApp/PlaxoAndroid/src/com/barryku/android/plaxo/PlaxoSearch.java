package com.barryku.android.plaxo;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
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
	public static String CUSTOM_SCHEME;
	public static final String FIELD_SEPARATOR = "~~";
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
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	    switch (item.getItemId()) {
		    case R.id.preference:
		    	Intent pref = new Intent(getBaseContext(), PlaxoPreference.class);
		    	startActivity(pref);
		    	break;
		    case R.id.history:
		    	TextView result = (TextView) findViewById(R.id.result);
		    	int historyCount = prefs.getInt(HISTORY_COUNT, 0);
		    	if (historyCount > 0) {
		    		Contact[] contacts = new Contact[historyCount];
		    		for (int i=0; i<historyCount; i++) {
		    			contacts[i] = getContactFromPhoneUri(prefs.getString(HISTORY_PREFIX + (i+1), ""));
		    		}
		    		result.setText(Html.fromHtml("<br>" + getContactHtml(contacts)));
		    	} else {
		    		result.setText(Html.fromHtml("<br>" +getString(R.string.msg_noRecord)));
		    	}
		    	break;
		    case R.id.lastSearch:
		    	String searchTerm = prefs.getString(HISTORY_SEARCH, "");
		    	EditText searchText = (EditText) findViewById(R.id.searchText);
		    	searchText.setText(searchTerm);
		    	performSearch(searchTerm);
		    	break;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	public String getContactHtml(Contact[] contacts) {
		StringBuffer htmlResult = new StringBuffer();
		for (int i=0; i<contacts.length; i++) {
			String contactName = contacts[i].getName();
			htmlResult.append("<b>").append(contactName).append("</b><br>");
			List<Phone> phones = contacts[i].getPhones();
			if (phones.size() > 0) {
				for (Phone phone:phones) {
					String phoneUri = contactName + FIELD_SEPARATOR + phone.getType() + 
						FIELD_SEPARATOR + phone.getNumber();
					htmlResult.append(phone.getType()).append(": ").append(
							"<a href=\"").append(CUSTOM_SCHEME).append("://").append(
							phoneUri).append("\">").append(
							phone.getNumber()).append("</a><br/>");
				}
			}
			htmlResult.append("<br>");
		}
		return htmlResult.toString();
	}
	
	private static final int MAX_HISTORY = 10;
	private static final String HISTORY_COUNT = "historyCount";
	private static final String HISTORY_PREFIX = "history_";
	@Override	
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Uri uri = intent.getData();
		String phoneUri = uri.toString().substring((CUSTOM_SCHEME + "://").length());
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		int historyCount = prefs.getInt(HISTORY_COUNT, 0);
		String history1 = prefs.getString(HISTORY_PREFIX + 1, "");
		if (!history1.equals(phoneUri)) {
			List<String> histories = new ArrayList<String>();
			histories.add(phoneUri);
			historyCount = historyCount < MAX_HISTORY ? historyCount+1 : MAX_HISTORY;
			if (historyCount > 1) {
				for (int i=1; i<historyCount; i++) {
					if (!prefs.getString(HISTORY_PREFIX + i,"").equals(phoneUri)) {
						histories.add(prefs.getString(HISTORY_PREFIX + i,""));
					}
				}
			}
			SharedPreferences.Editor editor = prefs.edit();
			int index = 0;
			for (String history:histories) {
				index++;
				editor.putString(HISTORY_PREFIX + index, history);
			}			
			editor.putInt(HISTORY_COUNT, historyCount);
			editor.commit();
		}
		Contact contact = getContactFromPhoneUri(phoneUri);
		intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact.getPhones().get(0).getNumber()));
		startActivity(intent);
	}
	
	private Contact getContactFromPhoneUri(String phoneUri) {
		Contact contact = new Contact();
		StringTokenizer st = new StringTokenizer(phoneUri, FIELD_SEPARATOR);
		contact.setName(st.nextToken());
		Phone phone = new Phone(st.nextToken(),st.nextToken());
		contact.addPhone(phone);
		return contact;
	}
	
	private static float TEXT_NORMAL_SIZE = 14;
	private static float TEXT_ZOOM_SIZE = 19;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	CUSTOM_SCHEME = getString(R.string.custom_scheme_tel);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final TextView result = (TextView) findViewById(R.id.result);
        	
        Button searchBtn = (Button) findViewById(R.id.searchBtn);        
        searchBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				EditText searchText = (EditText) findViewById(R.id.searchText);
		    	String searchTerm = searchText.getText().toString();
				performSearch(searchTerm);				
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
                	EditText searchText = (EditText) findViewById(R.id.searchText);
                	String searchTerm = searchText.getText().toString();
                	performSearch(searchTerm);
                  return true;
                }
                return false;
			}
        });  
        
    }
    private static final String HISTORY_SEARCH = "historySearch";
    private void performSearch(String searchTerm) {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	userName = prefs.getString("userName", getString(R.string.userName));
    	password = prefs.getString("password", getString(R.string.password));
    	TextView result = (TextView) findViewById(R.id.result);
    	result.setText("\n" + getString(R.string.msg_searching));    	
    	SharedPreferences.Editor editor = prefs.edit();
    	editor.putString(HISTORY_SEARCH, searchTerm);
    	editor.commit();
		ContactSearcher searcher = new ContactSearcher(parent);
		searcher.execute(searchTerm);
    }    
}