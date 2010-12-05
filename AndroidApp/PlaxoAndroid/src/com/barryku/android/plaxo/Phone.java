package com.barryku.android.plaxo;

public class Phone {
	private String type;
	private String number;
	
	public Phone(String type, String number) {
		this.type = type;
		this.number = number;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	
}
