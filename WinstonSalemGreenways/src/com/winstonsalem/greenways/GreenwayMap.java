package com.winstonsalem.greenways;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.winstonsalem.greenways.HelloItemizedOverlay;
import com.winstonsalem.greenways.ParseXMLTask;
import com.winstonsalem.greenways.R;
//import com.example.earthquake.Quake;
//import com.example.earthquake.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class GreenwayMap extends MapActivity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String str = "http://earthquake.usgs.gov/eqcenter/catalogs/1day-M2.5.xml";
	//private static String str = "C://Users/komal/Desktop/earthquake.xml";
	MapController mc;
	GeoPoint p;

	double lattitudeValue;
	double longitudeValue;
	
	//public static HashMap<String, Greenway> location;
	String provider;
	MyLocationOverlay myLocationOverlay;
	private MapView mapView;
	
	private final LocationListener locationListener = new LocationListener() {
    	public void onLocationChanged(Location location) {
    		updateWithNewLocation(location);
    	}
    	
    	public void onProviderDisabled(String provider){
    		updateWithNewLocation(null);
    	}
    	
    	public void onProviderEnabled(String provider){ }
    	
    	public void onStatusChanged(String provider, int status, Bundle extras){ }
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.greenway_map);
        
        // showing MapView
        mapView = (MapView) findViewById(R.id.mapView);
        
        mapView.setBuiltInZoomControls(true);
        mapView.displayZoomControls(true);
        mc = mapView.getController();
        
        //location = new HashMap<String, Greenway>();
        KMLParser parseXMLTask = new KMLParser(this);
        try {
			Greenway.greenways = parseXMLTask.execute(str).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //ParseXMLTask parseXMLTask = new ParseXMLTask();
        //parseXMLTask.execute(str);
        
        mapView.invalidate();
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.locationmarker);
        HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable, this);
        
        myLocationOverlay = new MyLocationOverlay(this, mapView);
        mapView.getOverlays().add(myLocationOverlay);
        myLocationOverlay.enableMyLocation();
       // System.out.println("title=" +Greenway.greenways.get("Strollway").getTitle());
        for(String key : Greenway.greenways.keySet()) {
        	
        	String[] l = Greenway.greenways.get(key).getLocation();
        	String title = Greenway.greenways.get(key).getTitle();
        	
        	lattitudeValue = Double.parseDouble(l[1]); //converting string lattitude value to double
	        longitudeValue=Double.parseDouble(l[0]); //converting string longitude value to double
	       // System.out.println("point=" +lattitudeValue);
	        GeoPoint point1 = new GeoPoint((int) (lattitudeValue * 1E6), (int) (longitudeValue * 1E6));
	        OverlayItem overlayitem = new OverlayItem(point1, title, "");
	
	        itemizedoverlay.addOverlay(overlayitem);
	        mapOverlays.add(itemizedoverlay);
        }
        
        
        LocationManager locationManager;
        String context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager)getSystemService(context);
        
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);
        updateWithNewLocation(location);
        locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);
    }
		

    public void updateWithNewLocation(Location location) {
    	
    	if (location != null) {
    		
    		Toast.makeText(
                        this,
                        "Current location:\nLatitude: " + location.getLatitude()
                                + "\n" + "Longitude: " + location.getLongitude(),
                        Toast.LENGTH_LONG).show();
            
    		// Update the map location.
    		Double geoLat = location.getLatitude()*1E6;
    		Double geoLng = location.getLongitude()*1E6;
    		GeoPoint point = new GeoPoint(geoLat.intValue(), geoLng.intValue());
    		
    		mc.setCenter(point);
            mc.zoomToSpan(point.getLatitudeE6(),point.getLongitudeE6());
            mc.setZoom(12);
         
	    	
    	} else {

                Toast.makeText(this, "Cannot fetch current location!",
                        Toast.LENGTH_LONG).show();
          
    	}
    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_front_line, menu);
        return true;
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}


	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}


	/* When the activity starts up, request updates */
    @Override
    protected void onResume() {
        super.onResume();
        
    }

    @Override
    protected void onPause() {
        super.onPause();
        
    }
}
