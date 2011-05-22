package com.barryku.boxnet.model;

public class FileItem extends Asset{
	private String id;
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
	
	@Override
	public String getName() {
		return fileName;
	}
	@Override
	public boolean isFolder() {
		return false;
	}
	
}
