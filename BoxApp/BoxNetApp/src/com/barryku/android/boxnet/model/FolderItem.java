package com.barryku.android.boxnet.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="folder", strict=false)
public class FolderItem extends BoxItem {
	@Attribute
	private String id;
	@Attribute
	private String name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
