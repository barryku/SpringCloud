package com.barryku.android.boxnet.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="response")
public class RequestToken {
	@Element
	private String status;
	@Element
	private String ticket;
	
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getTicket() {
		return ticket;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus() {
		return status;
	}
	
	public String getRequestToken() {
		return getTicket();
	}
	
}
