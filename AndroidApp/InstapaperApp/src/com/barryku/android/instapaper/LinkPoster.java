package com.barryku.android.instapaper;

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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import static com.barryku.android.instapaper.InstapaperActivity.*;

public class LinkPoster extends AsyncTask<String, Void, String> {
	private InstapaperActivity parent;
	private String linkUri;
	
	public LinkPoster(InstapaperActivity parent) {
		super();
		this.parent = parent;
	}
	
	@Override
	protected String doInBackground(String... params) {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
		HttpConnectionParams.setSoTimeout(httpParameters, 5000);
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);
		String result = null;
		try {					
			String requestUri = params[0];
			String userName = params[1];
			String password = params[2];
			linkUri = params[3];
			String url = String.format(requestUri, userName, password, linkUri);
			Log.d(LOG_TAG, "accessing:" + url);	
			result =  client.execute(new HttpGet(url), new ResponseHandler<String>() {

				public String handleResponse(HttpResponse response)
						throws ClientProtocolException, IOException {
					HttpEntity entity = response.getEntity();
					String httpResult = null;
					try {
						httpResult = convertStreamToString(entity.getContent());
					} catch (IOException e) {
						httpResult = e.getMessage();
					}
					return httpResult;
				}						
				
			});
		} catch (Exception e) {
			result = e.getMessage();
			Log.e(LOG_TAG, e.getMessage());
		} 
		return result;
	}					
	
	@Override
	protected void onPostExecute(String resultCode) {
		super.onPostExecute(resultCode);
		Log.d(LOG_TAG, "postExecute:[" + resultCode + "]");	
		String msg = null;
		String code = resultCode;
		if (resultCode != null) {
			code = resultCode.substring(0, 3);
			msg = RESPONSE_CODE.get(code);
		}  else {
			msg = "Unknown error.";
		}
		
		if (SUCCESSFUL_CODE.equals(code)) {
			Toast.makeText(parent, msg, Toast.LENGTH_SHORT).show();
				parent.doPostDone();
		} else {
			parent.saveLink(linkUri);
			Toast.makeText(parent, (msg==null? resultCode + "." : msg) + 
					" URL link is saved.", Toast.LENGTH_LONG).show();
		}						
	}
	
	private String convertStreamToString(InputStream is) {
		 
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
	
}
