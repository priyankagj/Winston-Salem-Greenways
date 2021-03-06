package com.winstonsalem.greenways;

import java.io.Serializable;
import java.util.ArrayList;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class HelloItemizedOverlay extends ItemizedOverlay<OverlayItem> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;

	public HelloItemizedOverlay(Drawable defaultMarker, Context context) {
	    super(boundCenterBottom(defaultMarker));
	    mContext = context;
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

	
	@Override
	protected boolean onTap(int index) {
	    OverlayItem item = mOverlays.get(index);
	    final String str = item.getSnippet();
	    
	    View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog, null);
	    AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	    dialog.setView(dialogView);
	    dialog.setTitle(item.getTitle());
	    dialog.setMessage(item.getSnippet());
	    dialog.show();
	    
	    dialogView.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
							
				Intent intent=new Intent(mContext, Greenway_Description.class);
				intent.putExtra("str", str);		
	 			mContext.startActivity(intent);				
			}
	    	
	    });
	    
	    return true;
	}
	
	@Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
    if(!shadow) {
        super.draw(canvas, mapView, false);
    }
}

	
}
