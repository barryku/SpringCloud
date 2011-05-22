package com.barryku.android.boxnet.model;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

@Root(name="response", strict=false)
public class AccountTree {
	@Element
	private String status;
	
	@ElementList(required=false)
	@Path("tree/folder")
	private List<FolderItem> folders;
	
	@ElementList(required=false)
	@Path("tree/folder")
	private List<FileItem> files;
	
	public List<FolderItem> getFolders() {
		return folders;
	}
	public void setFolders(List<FolderItem> folders) {
		this.folders = folders;
	}
	public List<FileItem> getFiles() {
		return files;
	}
	public void setFiles(List<FileItem> files) {
		this.files = files;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus() {
		return status;
	}
	
		
}
