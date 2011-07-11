package com.barryku.android.tryit.json;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FeatureMetadata {
	private String fid;
	private @JsonProperty("sesame_lookup") SesameLookup sesameLookup;
	
	public void setSesameLookup(SesameLookup sesameLookup) {
		this.sesameLookup = sesameLookup;
	}
	public SesameLookup getSesameLookup() {
		return sesameLookup;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public String getFid() {
		return fid;
	}
}
