package com.barryku.cloud.controller.helper;

public class TwilioSmsConfig {
	private String restUrl;
	private String fromNumber;
	
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


}
