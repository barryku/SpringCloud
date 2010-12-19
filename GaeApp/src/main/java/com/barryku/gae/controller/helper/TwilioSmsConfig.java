package com.barryku.gae.controller.helper;

public class TwilioSmsConfig {
	private String restUrl;
	private String fromNumber;
	private String sid;
	private String authToken;
	
	public String getRestUrl() {
		return restUrl;
	}

	public String getFromNumber() {
		return fromNumber;
	}

	public void setRestUrl(String restUrl) {
		this.restUrl = restUrl;
	}

	public void setFromNumber(String fromNumber) {
		this.fromNumber = fromNumber;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSid() {
		return sid;
	}


}
