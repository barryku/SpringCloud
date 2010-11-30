package com.barryku.android.plaxo;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PlaxoSearch extends Activity {
	private final PlaxoSearch parent = this;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button searchBtn = (Button) findViewById(R.id.searchBtn);        
        
        searchBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				performSearch();				
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
    	TextView result = (TextView) findViewById(R.id.result);
    	result.setText("\nsearching, please wait...");
    	EditText searchText = (EditText) findViewById(R.id.searchText);
		ContactSearcher searcher = new ContactSearcher(parent);
		searcher.execute(searchText.getText().toString());
    }
}