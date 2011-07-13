package com.barryku.android.hellomap;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.barryku.android.hellomap.json.FeatureMetadata;
import com.barryku.android.hellomap.json.GoogleMap;
import com.barryku.android.hellomap.json.InfoWindow;
import com.barryku.android.hellomap.json.Marker;
import com.barryku.android.hellomap.json.SesameLookup;
import com.barryku.android.hellomap.kml.KmlRoot;
import com.barryku.android.hellomap.kml.Placemark;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class HelloMapViewActivity extends MapActivity  {
	public static final String LOG_TAG = "hellomapapp";
	private MapView mapView;
	private List<Overlay> mapOverlays;
	private Drawable drawable;
	private HelloItemizedOverlay itemizedOverlay;
	private BalloonLayout noteBalloon;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
        setupBalloonLayout(); 
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapOverlays = mapView.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.androidmarker);
        itemizedOverlay = new HelloItemizedOverlay(this, drawable);        
        
        try {
        	//setupKmlOverlays();
			setupJsonOverlays();
			mapOverlays.add(itemizedOverlay);
			adjustMapZoomCenter();
		} catch (Exception e) {
			Log.e(LOG_TAG, e.getMessage(), e);
			e.printStackTrace();
		}		
    }
    
    private void adjustMapZoomCenter(){
		int minLat = Integer.MAX_VALUE;
		int maxLat = Integer.MIN_VALUE;
		int minLon = Integer.MAX_VALUE;
		int maxLon = Integer.MIN_VALUE;
		
		GeoPoint point = null;
		for (int i=0; i<itemizedOverlay.size(); i++) {
			point = itemizedOverlay.getItem(i).getPoint();
			int lat = point.getLatitudeE6();
			int lon = point.getLongitudeE6();

			maxLat = Math.max(lat, maxLat);
			minLat = Math.min(lat, minLat);
			maxLon = Math.max(lon, maxLon);
			minLon = Math.min(lon, minLon);
		}

		MapController mapController = mapView.getController();
		mapController.zoomToSpan(Math.abs(maxLat - minLat), Math.abs(maxLon
				- minLon));
		mapController.animateTo(new GeoPoint((maxLat + minLat) / 2, (maxLon + minLon) / 2));
		//mapController.animateTo(itemizedOverlay.getCenter()); //may need to call populate() first
    }
    
    private void setupKmlOverlays() throws Exception {
    	GeoPoint point = null;
    	InputStream is = getAssets().open("Ramen_in_Bay_Area.kml");
		Serializer serializer = new Persister();
		KmlRoot kml = serializer.read(KmlRoot.class, is);
		List<Placemark> markers = kml.getDocument().getPlacemarks();		
		for (Placemark marker: markers) {
			point = getGeoPointFromCoordiate(marker.getCoordinates());
			Log.d(LOG_TAG, marker.getName());
			itemizedOverlay.addOverlay(new OverlayItem(point, marker.getName(), marker.getDescription()));			
		}
    }
    
    private Map<Integer, String> placeUrlMap;
    private void setupJsonOverlays() throws Exception {
    	GeoPoint point = null;    	
		InputStream is = getAssets().open("Ramen_in_Bay_Area.json");
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
	    mapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
		is.skip(9);
		GoogleMap gmap = mapper.readValue(is, GoogleMap.class);	
		is.close();
		Log.d(LOG_TAG, gmap.getTitle());
		HashMap<String, String> storeNames = new HashMap<String, String>();
		
		for (Marker marker: gmap.getMsMap().getKmlOverlays().getMarkers()) {
			storeNames.put(marker.getFid(), marker.getName());
		}		
		
		placeUrlMap = new HashMap<Integer, String>();
		int index = 0;
		for (FeatureMetadata meta: gmap.getMsMap().getMetadata()) {
			SesameLookup sesame = meta.getSesameLookup();
			InfoWindow info = sesame == null ? null : sesame.getInfoWindow();
			point = new GeoPoint((int)(sesame.getLatitude() * 1E6), (int)(sesame.getLogitude() * 1E6));
			OverlayItem overlay = new OverlayItem(
					point,
					storeNames.get(meta.getFid()), "");
			itemizedOverlay.addOverlay(overlay);
			placeUrlMap.put(Integer.valueOf(index++), info.getPlaceUrl());
		}

    }
    
    private void setupBalloonLayout() {
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		noteBalloon = (BalloonLayout) layoutInflater.inflate(R.layout.balloon, null);
		
		TextView title = (TextView) noteBalloon.findViewById(R.id.note_txt);
		title.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				goPlace();
			}

		});
		
		ImageView goButton = (ImageView) noteBalloon.findViewById(R.id.go_button);
		goButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				goPlace();
			}

		});
    }
    
   
    private GeoPoint getGeoPointFromCoordiate(String coordinate) {
    	StringTokenizer st = new StringTokenizer(coordinate, ",");
    	float lo = Float.parseFloat(st.nextToken());
    	float la = Float.parseFloat(st.nextToken());
    	return new GeoPoint((int) (la * 1E6), (int) (lo * 1E6));
    }
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    private void goPlace() {
    	if (placeUrlMap != null) {
    		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(placeUrlMap.get(placeUrlIndex)));
    		startActivity(intent);
    	} else {
    		removeBalloonTip();
    	}
    }
    
    //the following methods are called by HelloItemizedOverlay.onTap();
    private int placeUrlIndex;    
    public void setPlaceUrlIndex(int index) {
    	placeUrlIndex = index;
    }    
 
	public void doTap(OverlayItem noteOverlay, String txt) {
    	mapView.removeView(noteBalloon);
    	noteBalloon.setVisibility(View.VISIBLE);
    	((TextView)noteBalloon.findViewById(R.id.note_txt)).setText(txt);
    	MapController mapController = mapView.getController();
    	mapController.animateTo(noteOverlay.getPoint());
    	mapView.addView(noteBalloon, new MapView.LayoutParams(12*txt.getBytes().length,55,noteOverlay.getPoint(),MapView.LayoutParams.BOTTOM_CENTER));
    }
    
    public void removeBalloonTip() {
    	noteBalloon.setVisibility(View.GONE);                    
    }
}