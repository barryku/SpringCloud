package com.barryku.cloud.service.helper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.HTTP;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class DropboxClient {
	private static Log log = LogFactory.getLog(DropboxClient.class);
	private Authenticator auth;
	private String apiVersion;
	private String restUrl; 
	private String httpRestUrl;
	private String userId;
	private String userPassword;
	
	
	
	public void setAuth(Authenticator auth) {
		this.auth = auth;
	}


	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public void setHttpRestUrl(String restUrl) {
		this.httpRestUrl = restUrl;
	}

	public void setRestUrl(String restUrl) {
		this.restUrl = restUrl;
	}


	public HttpResponse putBytes(String path, String assetName, ByteArrayInputStream asset) {
		HttpClient client = new DefaultHttpClient();

		try {	
			authorizeForm(auth.retrieveRequestToken(null),userId,userPassword);			
			auth.retrieveAccessToken("");
			String target = restUrl + apiVersion + "/files/dropbox" + path + "/";
			
			HttpPost req = new HttpPost(target);
			List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
			nvps.add(new BasicNameValuePair("file", assetName));
			req.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			
			auth.sign(req);

			// now we can add the real file multipart and we're good
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			InputStreamBodyWithSize bin = new InputStreamBodyWithSize(asset, assetName, asset.available());
			entity.addPart("file", bin);
			// this resets it to the new entity with the real file
			req.setEntity(entity);

			HttpResponse resp = client.execute(req);
			resp.getEntity().consumeContent();
			
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getFileList(String path) throws OAuthCommunicationException, IOException, OAuthException {
		authorizeForm(auth.retrieveRequestToken(null),userId,userPassword);			
		auth.retrieveAccessToken("");
		String target = httpRestUrl + apiVersion + "/files/dropbox" + path + "?file_limit=100&list=true&status_in_response=false";
		HttpUriRequest req = new HttpGet(target);
		auth.sign(req);
		HttpClient client = new DefaultHttpClient();

        HttpResponse response = client.execute(req);
        Map result = (Map) parseAsJson(response);
        List<HashMap<String, String>> fileList = (ArrayList) result.get("contents");
        ArrayList<String> files = new ArrayList<String>();
        for (HashMap<String, String> file: fileList) {
        	files.add(file.get("path").substring((path + "/").length()));
        }
        
		return files;
	}
	
	public HttpResponse getFile(String path, String fileName) throws OAuthCommunicationException, IOException, OAuthException {
		authorizeForm(auth.retrieveRequestToken(null),userId,userPassword);			
		auth.retrieveAccessToken("");		
		String target = restUrl + apiVersion + "/files/dropbox" + path + "/" + fileName;
		HttpClient client = new DefaultHttpClient();
		HttpGet req = new HttpGet(target);
		auth.sign(req);
		return client.execute(req);
	}
	
	private Object parseAsJson(HttpResponse response) throws IOException {
        Object result;
        String body = readResponse(response);

        if(response.getStatusLine().getStatusCode() != 200) {
            Map<String, Object> hm = new HashMap<String, Object>();
            hm.put("ERROR", response.getStatusLine());
            hm.put("RESULT", body);
            result = hm;
        } else {
            if(body.equals("OK")) {
                Map<String, String> hm = new HashMap<String, String>();
                hm.put("RESULT", body);
                result = hm;
            } else {
                try {
                    JSONParser parser = new JSONParser();
                    result = parser.parse(body);
                } catch(ParseException e) {
                    Map<String, Object> hm = new HashMap<String, Object>();
                    StatusLine strangeError = new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 999, "Bad JSON From Server: " + e.toString());
                    hm.put("ERROR", strangeError);
                    hm.put("RESULT", body);
                    result = hm;
                }
            }
        }
        return result;
    }    
	
	private String readResponse(HttpResponse response) throws IOException {
        HttpEntity ent = response.getEntity();
        if (ent != null) {
            BufferedReader in = new BufferedReader(new InputStreamReader(ent.getContent()), 2048);
            String inputLine = null;
            String result = "";
    
            while((inputLine = in.readLine()) != null) {
                result += inputLine;
            }
    
            response.getEntity().consumeContent();
            return result;
        } else {
            return "";
        }
    }
	
    private void authorizeForm(String url, String userId, String userPassword) throws IOException {
        assert url != null : "You must give a url.";
        assert userId != null : "You gave a null testing_user.";
        assert userPassword != null : "You gave a null testing_password.";

        log.info("AUTHORIZING: " + url);

        WebClient webClient = new WebClient();
        webClient.setJavaScriptEnabled(false);

        HtmlPage page = (HtmlPage)webClient.getPage(url);
        HtmlForm form = (HtmlForm)page.getForms().get(1);
        HtmlSubmitInput button = (HtmlSubmitInput)form.getInputByValue("Log in");


        HtmlTextInput emailField = (HtmlTextInput)form.getInputByName("login_email");
        emailField.setValueAttribute(userId);

        HtmlPasswordInput password = (HtmlPasswordInput)form.getInputByName("login_password");
        password.setValueAttribute(userPassword);

        // Now submit the form by clicking the button and get back the second page.
        HtmlPage page2 = (HtmlPage)button.click();

        try {
            form = (HtmlForm)page2.getForms().get(1);
            button = (HtmlSubmitInput)form.getInputByValue("Allow");
            button.click();
        } catch(ElementNotFoundException e) {
            log.info("No allow button, must be already approved.");
        } catch(IndexOutOfBoundsException e) {
            log.info("No second form, must be already approved.");
        }
    }


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

}
