package com.barryku.android.tryit;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class HelloItemizedOverlay extends ItemizedOverlay {
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Activity parent;
	public HelloItemizedOverlay(Activity parentActivity, Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		this.parent = parentActivity;
	}

	
	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mOverlays.size();
	}
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	@Override
	protected boolean onTap(int i) {
		Toast.makeText(parent,mOverlays.get(i).getTitle(),Toast.LENGTH_SHORT).show(); 
		return(true); 
	}
	
	
}
