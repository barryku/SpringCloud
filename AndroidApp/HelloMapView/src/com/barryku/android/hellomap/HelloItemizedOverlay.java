package com.barryku.android.hellomap;

import static com.barryku.android.hellomap.HelloMapViewActivity.LOG_TAG;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class HelloItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private HelloMapViewActivity parent;
	public HelloItemizedOverlay(HelloMapViewActivity parentActivity, Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		this.parent = parentActivity;
	}
	
	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	@Override
	public boolean onTap(GeoPoint point, MapView view) {
		Log.d(LOG_TAG, "onTap point/view: " + point + "," + view);
		parent.removeBaloonTip();
		return super.onTap(point, view);
	}
	
	@Override
	protected boolean onTap(int i) {
		Log.d(LOG_TAG, "onTap: " + i);
		OverlayItem overlay = mOverlays.get(i);
		parent.setPlaceUrlIndex(i);
		parent.doTap(overlay, overlay.getTitle());
		return(true); 
	}
	
	
}
