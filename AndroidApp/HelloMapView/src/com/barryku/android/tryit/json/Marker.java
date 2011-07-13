package com.barryku.android.tryit.json;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Marker {
	private String name;
	private String image;
	private String fid;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public String getFid() {
		return fid;
	}
	
	
}
