package com.winstonsalem.greenways;

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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Greenway_Description extends Activity{

	HashMap<String, Greenway> greenwayHashMap;
	String provider;

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	/*	getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
				| ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
		*/
		//Put the following code for disabling window title after onCreate and before setContentView, else it won't work
		setContentView(R.layout.description);
		
		ButtonPanel btn = (ButtonPanel) findViewById(R.id.buttonPanel);
		btn.initHeader(this);
		
		greenwayHashMap = Greenway.greenways;

		String str = this.getIntent().getStringExtra("str");
		
		TextView nameGreenWay = (TextView) findViewById(R.id.nameGreenWay);
		nameGreenWay.setText(greenwayHashMap.get(str).getTitle()+ " at " + greenwayHashMap.get(str).getAccesspt());
		
		TextView nameAccessPoint = (TextView) findViewById(R.id.accessPointName);
		System.out.println(nameAccessPoint);
		//nameAccessPoint.setText();
		
		String[] l = greenwayHashMap.get(str).getLocation();
		final double lattitudeValue = Double.parseDouble(l[1]); //converting string latitude value to double
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
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case R.id.menu_map:
			Intent map = new Intent(Greenway_Description.this, GreenwayMap.class);     
			startActivity(map);
			break;
		case R.id.menu_weather:
			Weather.inital(this);
			break;
		case R.id.menu_citylink:
			Intent citylink = new Intent(Greenway_Description.this, CityLink.class);     
			startActivity(citylink);
			break;
		default:
			break;
		}
		return true;
	}*/
}
