package com.barryku.android.hellomap.json;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class InfoWindow {
	private @JsonProperty("place_url") String placeUrl;
	private @JsonProperty("hp") Homepage homepage;
	
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Homepage {
		private String url;
		private @JsonProperty("actual_url") String actualUrl;
		public void setUrl(String url) {
			this.url = url;
		}
		public String getUrl() {
			return url;
		}
		public void setActualUrl(String actualUrl) {
			this.actualUrl = actualUrl;
		}
		public String getActualUrl() {
			return actualUrl;
		}
	}
	
	public String getHomePageUrl() {
		return homepage == null? "" : homepage.getActualUrl();
	}
	
	public String getPlaceUrl() {
		return placeUrl == null ? null : placeUrl.replaceAll("x26", "&");
	}

	public void setPlaceUrl(String placeUrl) {
		this.placeUrl = placeUrl;
	}

	public Homepage getHomepage() {
		return homepage;
	}

	public void setHomepage(Homepage homepage) {
		this.homepage = homepage;
	}
}
