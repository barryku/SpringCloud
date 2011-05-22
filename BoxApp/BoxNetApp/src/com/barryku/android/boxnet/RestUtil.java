package com.barryku.android.boxnet;

import static com.barryku.android.boxnet.Constants.LOG_TAG;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import android.util.Log;

public class RestUtil {
	public static RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		FormHttpMessageConverter formConverter = new FormHttpMessageConverter();
		formConverter.setCharset(Charset.forName("UTF8"));
		restTemplate.getMessageConverters().add(formConverter);
		
		restTemplate.setErrorHandler(new ResponseErrorHandler() {
			
			public boolean hasError(ClientHttpResponse resp) throws IOException {	
				HttpStatus status = resp.getStatusCode();
				if (HttpStatus.CREATED.equals(status) || HttpStatus.OK.equals(status)) {
					return false;
				} else {
					Log.d(LOG_TAG, "response: " + resp.getBody());
					return true;
				}
			}
			
			public void handleError(ClientHttpResponse resp) throws IOException {
				throw new HttpClientErrorException(resp.getStatusCode());
			}
		});
    	return restTemplate;
	}
}
