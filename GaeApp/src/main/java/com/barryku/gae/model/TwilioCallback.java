package com.barryku.gae.model;

import java.io.Serializable;
//see http://www.twilio.com/docs/api/2010-04-01/twiml/sms/twilio_request
public class TwilioCallback implements Serializable{
	private static final long serialVersionUID = -820978233633870702L;
	private String smsSid;
	private String accountSid;
	private String from;
	private String to;
	private String body;
	private String fromCity;
	private String fromState;
	private String fromZip;
	private String fromCountry;
	private String toCity;
	private String toState;
	private String toZip;
	private String toCountry;
	public String getSmsSid() {
		return smsSid;
	}
	public void setSmsSid(String smsSid) {
		this.smsSid = smsSid;
	}
	public String getAccountSid() {
		return accountSid;
	}
	public void setAccountSid(String accountSid) {
		this.accountSid = accountSid;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getFromCity() {
		return fromCity;
	}
	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}
	public String getFromState() {
		return fromState;
	}
	public void setFromState(String fromState) {
		this.fromState = fromState;
	}
	public String getFromZip() {
		return fromZip;
	}
	public void setFromZip(String fromZip) {
		this.fromZip = fromZip;
	}
	public String getFromCountry() {
		return fromCountry;
	}
	public void setFromCountry(String fromCountry) {
		this.fromCountry = fromCountry;
	}
	public String getToCity() {
		return toCity;
	}
	public void setToCity(String toCity) {
		this.toCity = toCity;
	}
	public String getToState() {
		return toState;
	}
	public void setToState(String toState) {
		this.toState = toState;
	}
	public String getToZip() {
		return toZip;
	}
	public void setToZip(String toZip) {
		this.toZip = toZip;
	}
	public String getToCountry() {
		return toCountry;
	}
	public void setToCountry(String toCountry) {
		this.toCountry = toCountry;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
	
}
