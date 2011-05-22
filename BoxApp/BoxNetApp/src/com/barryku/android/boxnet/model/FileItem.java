package com.barryku.android.boxnet.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="file", strict=false)
public class FileItem extends BoxItem {
	@Attribute
	private String id;
	@Attribute(name="file_name")
	private String fileName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getName() {
		return getFileName();
	}
}
