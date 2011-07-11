package com.barryku.android.tryit;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.barryku.android.tryit.json.FeatureMetadata;
import com.barryku.android.tryit.json.GoogleMap;
import com.barryku.android.tryit.json.InfoWindow;
import com.barryku.android.tryit.json.Marker;
import com.barryku.android.tryit.json.SesameLookup;
import com.barryku.android.tryit.kml.KmlRoot;
import com.barryku.android.tryit.kml.Placemark;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class HelloMapViewActivity extends MapActivity  {
	//private LinearLayout linearLayout;
	private MapView mapView;
	private List<Overlay> mapOverlays;
	private Drawable drawable;
	private HelloItemizedOverlay itemizedOverlay;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapOverlays = mapView.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.androidmarker);
        itemizedOverlay = new HelloItemizedOverlay(this, drawable);
        GeoPoint point = new GeoPoint(19240000,-99120000);
        //OverlayItem overlayitem = new OverlayItem(point, "", "");
        //itemizedOverlay.addOverlay(overlayitem);
        
        /**
                
        try {
			InputStream is = getAssets().open("Ramen_in_Bay_Area.kml");
			Serializer serializer = new Persister();
			KmlRoot kml = serializer.read(KmlRoot.class, is);
			List<Placemark> markers = kml.getDocument().getPlacemarks();
			for (Placemark marker: markers) {
				point = getGeoPoint(marker.getCoordinates());
				Log.d("marker test...", marker.getName());
				itemizedOverlay.addOverlay(new OverlayItem(getGeoPoint(marker.getCoordinates()), marker.getName(), marker.getDescription()));
				
			}
		} catch (Exception e) {
			Log.e("test...", e.getMessage(), e);
			e.printStackTrace();
		}
		**/
        
        try {
			InputStream is = getAssets().open("Ramen_in_Bay_Area.json");
			ObjectMapper mapper = new ObjectMapper();
			//mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
		    mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		    mapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
			is.skip(9);
			GoogleMap gmap = mapper.readValue(is, GoogleMap.class);	
			is.close();
			Log.d("test111 result:", gmap.getTitle());
			HashMap<String, String> storeNames = new HashMap<String, String>();
			
			for (Marker marker: gmap.getMsMap().getKmlOverlays().getMarkers()) {
				storeNames.put(marker.getFid(), marker.getName());
			}
			
			for (FeatureMetadata meta: gmap.getMsMap().getMetadata()) {
				SesameLookup sesame = meta.getSesameLookup();
				InfoWindow info = sesame == null ? null : sesame.getInfoWindow();
				OverlayItem overlay = new OverlayItem(
						new GeoPoint((int)(sesame.getLatitude() * 1E6), (int)(sesame.getLogitude() * 1E6)),
						storeNames.get(meta.getFid()), "");
				overlay.setMarker(new Drawable())
				itemizedOverlay.addOverlay(overlay);
			}
		} catch (Exception e) {
			Log.e("test...", e.getMessage(), e);
			e.printStackTrace();
		}
		
		mapOverlays.add(itemizedOverlay);
		MapController mapController = mapView.getController();
		mapController.animateTo(point);
    }
    
    private GeoPoint getGeoPoint(String coordinate) {
    	StringTokenizer st = new StringTokenizer(coordinate, ",");
    	float lo = Float.parseFloat(st.nextToken());
    	float la = Float.parseFloat(st.nextToken());
    	return new GeoPoint((int) (la * 1E6), (int) (lo * 1E6));
    }
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}