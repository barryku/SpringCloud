package com.barryku.camel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.util.MessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.googleapis.auth.storage.GoogleStorageAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;

@Component
public class GsFileManager {	
	private static final SimpleDateFormat httpDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
	private static final Logger logger = LoggerFactory.getLogger(GsFileManager.class);
	static{
		httpDateFormat.setTimeZone(TimeZone.getTimeZone("pst"));
	}

	public GsFileManager() {}
	
	public void process(Message message) throws IOException {
		
		String fileName = message.getHeader(Exchange.FILE_NAME, String.class);
		String type = MessageHelper.getContentType(message);
		type = type == null ? "text/html" : type;
		byte[] content = message.getBody(byte[].class);
		logger.info("saving " + fileName + ", " + type);
		putFile(fileName, type, content);
	}
	
	
	private @Value("${gs.apiKey}") String apiKey;
	private @Value("${gs.apiKeySecret}") String apiKeySecret;
	private @Value("${gs.bucket}") String bucket;
	private @Value("${gs.url}") String url;
	
	private void putFile(String fileName, String type, byte[] content) throws IOException {
		HttpTransport transport = GoogleTransport.create();
		GoogleStorageAuthentication.authorize(transport, apiKey, apiKeySecret);
		
		HttpRequest request = transport.buildPutRequest();
		
		
		InputStreamContent isc = new InputStreamContent();
		isc.inputStream = new ByteArrayInputStream(content);
		isc.type = type;
		request.content = isc;
		request.url = new GenericUrl(url + bucket + "/" + URLEncoder.encode(fileName, "utf8"));		
		GoogleHeaders headers = (GoogleHeaders) request.headers;
		headers.date = httpDateFormat.format(new Date());
		try {
			request.execute();			
		} catch (HttpResponseException e) {
			logger.warn(getStreamContent(e.response.getContent()), e);
		}
	}
	
	public static void main(String... args) throws Exception {
		
		HttpTransport transport = GoogleTransport.create();
		//transport.addParser(new XmlHttpParser());
	    GoogleStorageAuthentication.authorize(transport, "mykey", "myKeySecret...."); 

	    HttpRequest request = transport.buildGetRequest();
	    request.url= new GenericUrl("http://agilecloud.commondatastorage.googleapis.com/");
   
	    GoogleHeaders headers = (GoogleHeaders) request.headers;
	    headers.date = httpDateFormat.format(new Date());

	    try {
	    	
	    	HttpResponse response = request.execute();
	    	logger.info(getStreamContent(response.getContent()));
	    	
	    } catch (HttpResponseException e) {
	    	logger.warn(getStreamContent(e.response.getContent()), e);
	    }
	}
	
	private static String getStreamContent(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1;) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
	}
}
