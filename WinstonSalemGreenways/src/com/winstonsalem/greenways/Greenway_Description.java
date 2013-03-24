package com.winstonsalem.greenways;

import java.io.Serializable;
import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Greenway_Description extends Activity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	HashMap<String, Greenway> location;
	String provider;
	//GreenwayMap m;
	
	private final LocationListener locationListener = new LocationListener() {
    	public void onLocationChanged(Location location) {
    		//m.updateWithNewLocation(location);
    	}
    	
    	public void onProviderDisabled(String provider){
    		//m.updateWithNewLocation(null);
    	}
    	
    	public void onProviderEnabled(String provider){ }
    	
    	public void onStatusChanged(String provider, int status, Bundle extras){ }
    };
    
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    
    	//Put the following code for disabling window title after onCreate and before setContentView, else it won't work
        setContentView(R.layout.description);
        
        Bundle bundle = this.getIntent().getExtras();
        //System.out.println("bundle= " + bundle.getSerializable("str"));
     //if(bundle!=null){
          
    	 //String str = (String) bundle.getSerializable("str");
    	 
       // m= new GreenwayMap();
          GreenwayMap g = new GreenwayMap();
          Intent intent = g.getIntent();
        //location = (HashMap<String, Greenway>)intent.getSerializableExtra("str");
        String str = bundle.getString("str");
        //System.out.println("bundle= " +str);
        
        location = Greenway.greenways;
        String[] l = location.get(str).getLocation();
    	//String title = location.get("Southern Alaska").getTitle();
		final double lattitudeValue = Double.parseDouble(l[1]); //converting string lattitude value to double
        final double longitudeValue=Double.parseDouble(l[0]); //converting string longitude value to double
        
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
        //m.updateWithNewLocation(location);
        locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);
        
        final double geoLat = location.getLatitude();
		final double geoLng = location.getLongitude();
		
        Button buttonOne = (Button) findViewById(R.id.get_direction);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
            			Uri.parse("http://maps.google.com/maps?daddr="+lattitudeValue+","+longitudeValue+"&saddr="+geoLat+","+geoLng));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
			}
        });
 
     }
    //}
 
}
