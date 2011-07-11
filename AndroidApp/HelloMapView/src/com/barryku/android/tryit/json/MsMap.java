package com.barryku.android.tryit.json;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class MsMap {
	private String name;
	private String description;
	private KmlOverlay kmlOverlays;
	private @JsonProperty("feature_metadata") List<FeatureMetadata> metadata;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setKmlOverlays(KmlOverlay kmlOverlays) {
		this.kmlOverlays = kmlOverlays;
	}
	public KmlOverlay getKmlOverlays() {
		return kmlOverlays;
	}
	public void setMetadata(List<FeatureMetadata> metadata) {
		this.metadata = metadata;
	}
	public List<FeatureMetadata> getMetadata() {
		return metadata;
	}
	

}
