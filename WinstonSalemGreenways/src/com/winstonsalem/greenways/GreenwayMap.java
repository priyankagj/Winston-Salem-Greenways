package com.winstonsalem.greenways;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.winstonsalem.greenways.HelloItemizedOverlay;
import com.winstonsalem.greenways.ParseXMLTask;
import com.winstonsalem.greenways.R;
//import com.example.earthquake.Quake;
//import com.example.earthquake.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MapView.LayoutParams;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GreenwayMap extends MapActivity{
	private static String str = "http://earthquake.usgs.gov/eqcenter/catalogs/1day-M2.5.xml";
	//private static String str = "C://Users/komal/Desktop/earthquake.xml";
	MapController mc;
	GeoPoint p;

	double lattitudeValue;
	double longitudeValue;
	
	HashMap<String, Greenway> location;
	String provider;
	MyLocationOverlay myLocationOverlay;
	private MapView mapView;
	private LocationManager locManager;
	
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
    
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.greenway_map);
        
        // showing MapView
        mapView = (MapView) findViewById(R.id.mapView);
        
        mapView.setBuiltInZoomControls(true);
        mapView.displayZoomControls(true);
        mc = mapView.getController();
        
        location = new HashMap<String, Greenway>();
        ParseXMLTask parseXMLTask = new ParseXMLTask();
        try {
			location = parseXMLTask.execute(str).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        mapView.invalidate();
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.locationmarker);
        HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable, this);
        
        myLocationOverlay = new MyLocationOverlay(this, mapView);
        mapView.getOverlays().add(myLocationOverlay);
        myLocationOverlay.enableMyLocation();
        
        for(String key : location.keySet()) {
        	String[] l = location.get(key).getLocation();
        	String title = location.get(key).getTitle();
			lattitudeValue = Double.parseDouble(l[0]); //converting string lattitude value to double
	        longitudeValue=Double.parseDouble(l[1]); //converting string longitude value to double
			
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
		

    private void updateWithNewLocation(Location location) {
    	
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
            mc.setZoom(17);
         
	    	
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
