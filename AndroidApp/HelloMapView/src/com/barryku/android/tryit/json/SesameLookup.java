package com.barryku.android.tryit.json;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class SesameLookup {
	private String image;
	private @JsonProperty("laddr") String address;
	@JsonProperty("sxph")
	private String phone;
	private InfoWindow infoWindow;
	private LatLng latlng;
	
	public static class LatLng {
		private String lat;
		private String lng;
		public void setLat(String lat) {
			this.lat = lat;
		}
		public String getLat() {
			return lat;
		}
		public void setLng(String lng) {
			this.lng = lng;
		}
		public String getLng() {
			return lng;
		}
		
	}
	
	public float getLatitude() {
		return (Float.parseFloat(latlng.getLat()));		
	}
	
	public float getLogitude() {
		return (Float.parseFloat(latlng.getLng()));	
	}
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setInfoWindow(InfoWindow infoWindow) {
		this.infoWindow = infoWindow;
	}
	public InfoWindow getInfoWindow() {
		return infoWindow;
	}
	public void setLatlng(LatLng latlng) {
		this.latlng = latlng;
	}
	public LatLng getLatlng() {
		return latlng;
	}
	
	
}
