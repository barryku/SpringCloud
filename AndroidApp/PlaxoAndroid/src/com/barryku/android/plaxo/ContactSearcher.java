package com.barryku.android.plaxo;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

public class ContactSearcher extends AsyncTask<String, Void, String> {
	
	private PlaxoSearch parent;
	
	public ContactSearcher(PlaxoSearch parent) {
		super();
		this.parent = parent;
	}
	
	private static final int AUTH_ERROR_CODE = 401;
	private static final String AUTH_ERROR = "BARRYKU_AUTH_ERROR";
	@Override
	protected String doInBackground(String... params) {
		String result = null;		
		String user = parent.getUserName();
		String password = parent.getPassword();
		String url = parent.getString(R.string.plaxoUrl) + URLEncoder.encode(params[0]);
		DefaultHttpClient client = new DefaultHttpClient();
		
		client.getCredentialsProvider().setCredentials(new AuthScope(null, -1),
				new UsernamePasswordCredentials(user, password));
		try {
			HttpGet request = new HttpGet(url);
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == AUTH_ERROR_CODE) {
				result = AUTH_ERROR;
			} else {
				result = EntityUtils.toString(response.getEntity());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private static final String PHONE_ICON = "phone.png";	

	@Override
	protected void onPostExecute(String result) {
		StringBuffer htmlResult = new StringBuffer("<br>");
		if (AUTH_ERROR.equals(result)) {
			htmlResult.append(parent.getString(R.string.msg_authFailed));
		} else {
			if (result == null) {
				Toast.makeText(parent, parent.getString(R.string.msg_searchFailed), Toast.LENGTH_LONG).show();
			} else {
				Contact[] contacts = getContacts(result);
				if (contacts != null) {
					htmlResult.append(parent.getContactHtml(contacts));	
				} else {
					htmlResult.append(parent.getString(R.string.msg_noRecord));
				}
			}
		}
		
		TextView resultView = (TextView) parent.findViewById(R.id.result);
		resultView.setText(Html.fromHtml(htmlResult.toString(),new Html.ImageGetter() {
			
			@Override
			public Drawable getDrawable(String imgSrc) {
				int id = R.drawable.noutfound;
				if (PHONE_ICON.equals(imgSrc)) {
					id = R.drawable.phone;					
				} 				
				Drawable img = parent.getResources().getDrawable(id);
				img.setBounds(0,0,img.getIntrinsicWidth(),img.getIntrinsicHeight());
				return img;					
			}
		},null));
		resultView.setMovementMethod(LinkMovementMethod.getInstance());
	}	

			
	private Contact[] getContacts(String jsonString) {
		Contact[] result = null;
		try {
			JSONObject contacts = new JSONObject(jsonString);
			int total = contacts.getInt("totalResults");
			if (total > 0) {
				int itemsPerPage = contacts.getInt("itemsPerPage");
				result = new Contact[total>itemsPerPage ? itemsPerPage : total];
				JSONArray contactList = contacts.getJSONArray("entry");
				for (int i=0; i<total; i++) {
					JSONObject contactObj = contactList.getJSONObject(i);
					Contact contact = new Contact();					
					contact.setName(contactObj.getString("displayName"));
					result[i] = contact;
					try {
						JSONArray phoneList = contactObj.getJSONArray("phoneNumbers");
						if (phoneList.length() > 0) {
							for (int j=0; j<phoneList.length(); j++) {
								JSONObject phoneObj = phoneList.getJSONObject(j);
								contact.addPhone(new Phone(
										phoneObj.getString("type"), phoneObj.getString("value")));
							}
						}
					} catch (JSONException je) {
						//skip, if no phone number
					}
				}				
			}
		} catch (JSONException e) {			
			e.printStackTrace();
		}
		
		return result;
	}

}
