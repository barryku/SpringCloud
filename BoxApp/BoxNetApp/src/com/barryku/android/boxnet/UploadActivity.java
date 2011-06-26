package com.barryku.android.boxnet;

import static com.barryku.android.boxnet.Constants.AUTH_TOKEN_KEY;
import static com.barryku.android.boxnet.Constants.LAST_VIEWED_FOLDER;

import java.io.File;
import java.util.Arrays;

import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import static com.barryku.android.boxnet.Constants.*;

public class UploadActivity extends ListActivity {
	private String uploadUri;
	private String authToken;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload);
		uploadUri = getString(R.string.upload_uri);
	
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		final String folderId = prefs.getString(LAST_VIEWED_FOLDER, "0");
		authToken = prefs.getString(AUTH_TOKEN_KEY, "");	
		
		//getDir() returns app specific folder e.g. data/package/app_... 
		//File dir = getDir(getString(R.string.uploader_folder), MODE_WORLD_READABLE);
		
		Log.d(LOG_TAG, "dir: " + Environment.getExternalStorageDirectory().getPath()+ File.separator + "download");
		Log.d(LOG_TAG, "dirP: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
		//File dir = new File(getString(R.string.uploader_folder));
		final File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		String files[] = dir.list();
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Arrays.asList(files)));
		final ListView v = getListView();
		v.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(final AdapterView<?> parentView, View view, int position,
					long id) {
				final String fileName = (String) parentView.getItemAtPosition(position);
				new AlertDialog.Builder(parentView.getContext()).setTitle("Upload").setMessage("Upload " + fileName +"?")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface di, int arg1) {
							String fileToUpload = dir.getPath() + File.separator + fileName;
							MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
							parts.add("file", new FileSystemResource(fileToUpload));
							RestTemplate rest = RestUtil.getRestTemplate();
							String response = rest.postForObject(uploadUri, parts, String.class, authToken, folderId);
							Log.d(LOG_TAG, response);
							Intent browseIntent = new Intent(parentView.getContext(), BrowseActivity.class);
					    	startActivity(browseIntent);
						}
					}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface di, int arg1) {
							di.dismiss();							
						}
					}).show();
			}
			
		});
		
	}
}
