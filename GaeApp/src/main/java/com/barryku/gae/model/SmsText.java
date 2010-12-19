package com.barryku.gae.model;

import java.io.Serializable;

public class SmsText implements Serializable{
	private static final long serialVersionUID = -1358827998942007371L;
	private String from;
	private String sendTo;
	private String text;
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getSendTo() {
		return sendTo;
	}
	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
}
