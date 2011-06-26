package com.barryku.android.instapaper;

public class UrlLink {
	private int id;
	private String link;
	private int lastUpdated;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public int getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(int lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	public String toString() {
		return link.length() > 25 ? link.substring(0, 25) + "..." : link;
	}
}
