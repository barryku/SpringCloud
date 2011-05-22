package com.barryku.android.boxnet.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="response")
public class RestResponse {
	@Element
	private String status;


	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus() {
		return status;
	}

	
}
