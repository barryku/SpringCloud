package com.barryku.android.plaxo;

import java.util.ArrayList;
import java.util.List;

public class Contact {
	private String name;
	private List<Phone> phones = new ArrayList<Phone>();
	
	public void addPhone(Phone phone) {
		phones.add(phone);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Phone> getPhones() {
		return phones;
	}

}
