package com.barryku.android.tryit.json;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class GoogleMap {
	private String title;
	private String url;
	
	@JsonProperty("ms_map")
	private MsMap msMap;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url == null ? null : url.replaceAll("x26", "&");
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public MsMap getMsMap() {
		return msMap;
	}

	public void setMsMap(MsMap msMap) {
		this.msMap = msMap;
	}
	
}
