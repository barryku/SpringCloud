package com.barryku.cloud.model;

public class SpringProperties {
	private String userName;
	private String password;
	private String securityToken;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSecurityToken() {
		System.out.println("security:" + securityToken);
		return securityToken;
	}
	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}
	
	
}
