package com.barryku.boxnet.service;

public class UnauthenticatedException extends Exception {
	private static final long serialVersionUID = 1L;
	private String requestUrl;
	private String authUrl;
	
	public String getRequestUrl() {
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	public String getAuthUrl() {
		return authUrl;
	}
	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}
}
