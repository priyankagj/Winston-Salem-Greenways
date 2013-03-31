package com.winstonsalem.greenways;

import java.io.Serializable;
import java.util.ArrayList;
import android.content.Context;
import android.graphics.drawable.Drawable;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class ParkingItemizedOverlay extends ItemizedOverlay<OverlayItem> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	public ParkingItemizedOverlay(Drawable defaultMarker, Context context) {
	    super(boundCenterBottom(defaultMarker));
	}

	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
	    return mOverlays.get(i);
	}

	@Override
	public int size() {
	    return mOverlays.size();
	}

}
