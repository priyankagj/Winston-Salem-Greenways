package com.winstonsalem.greenways;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.winstonsalem.greenways.HelloItemizedOverlay;
import com.winstonsalem.greenways.ParkingParse;
import com.winstonsalem.greenways.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.content.Context;
import android.widget.Toast;

public class GreenwayMap extends MapActivity{
	MapController mc;
	GeoPoint p;

	double lattitudeValue;
	double longitudeValue;

	double lattitudeValue2;
	double longitudeValue2;

	String provider;
	MyLocationOverlay myLocationOverlay;
	private MapView mapView;

	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location, 1);
		}

		public void onProviderDisabled(String provider){
			updateWithNewLocation(null, 1);
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

		myLocationOverlay = new MyLocationOverlay(this, mapView);
		myLocationOverlay.enableMyLocation();
		mapView.getOverlays().add(myLocationOverlay);

		displayAccesspt();

		displayParkingArea();

		//displayGreenway();

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
		updateWithNewLocation(location, 0);
		locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);
	}

	@SuppressWarnings("unused")
	private void displayGreenway() {

		ArrayList<String[]> line = new ArrayList<String[]>();
		GreenwayParse parseXMLTask = new GreenwayParse(this);

		try {
			line = parseXMLTask.execute("greenway").get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mapView.invalidate();

		Iterator<String[]> itr = line.iterator();
		String[] l = new String[3];
		String[] l2 = new String[3];
		GeoPoint gp1 = null;
		GeoPoint gp2 = null;
		int count = 0;
		int itrcount = 0;
		while(itr.hasNext()){	
			itrcount++;
			String[] ls = itr.next();
			count += ls.length;
			if(ls.length > 10){
				for(int i=0;i<ls.length-6;i=i+6){
					l = ls[i].split(",");
					lattitudeValue = Double.parseDouble(l[1]); //converting string latitude value to double
					longitudeValue=Double.parseDouble(l[0]); //converting string longitude value to double
					gp1 = new GeoPoint((int) (lattitudeValue * 1E6), (int) (longitudeValue * 1E6));

					if(i+6 > ls.length-2){
						l2 = ls[ls.length-2].split(",");
					}
					else{
						l2 = ls[i+6].split(",");
					}
					lattitudeValue2 = Double.parseDouble(l2[1]); //converting string latitude value to double
					longitudeValue2 =Double.parseDouble(l2[0]); //converting string longitude value to double
					gp2 = new GeoPoint((int) (lattitudeValue2 * 1E6), (int) (longitudeValue2 * 1E6));

					mapView.getOverlays().add(new LineItemizedOverlay(gp1, gp2));
				}
			}
			else{
				for(int i=0;i<ls.length-4;i=i+3){
					l = ls[i].split(",");
					lattitudeValue = Double.parseDouble(l[1]); //converting string lattitude value to double
					longitudeValue=Double.parseDouble(l[0]); //converting string longitude value to double
					gp1 = new GeoPoint((int) (lattitudeValue * 1E6), (int) (longitudeValue * 1E6));

					if(i+3 > ls.length-2){
						l2 = ls[ls.length-2].split(",");
					}
					else{
						l2 = ls[i+3].split(",");
					}
					lattitudeValue2 = Double.parseDouble(l2[1]); //converting string lattitude value to double
					longitudeValue2 =Double.parseDouble(l2[0]); //converting string longitude value to double
					gp2 = new GeoPoint((int) (lattitudeValue2 * 1E6), (int) (longitudeValue2 * 1E6));

					mapView.getOverlays().add(new LineItemizedOverlay(gp1, gp2));
				}
			}
		}
		System.out.println("count= " +count);
		System.out.println("itrcount= " +itrcount);
	}

	private void displayParkingArea() {
		HashMap<String, String[]> parking = new HashMap<String, String[]>();
		ParkingParse parseXMLTask = new ParkingParse(this);
		try {
			parking = parseXMLTask.execute("parking").get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mapView.invalidate();

		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.parkingmarker);
		ParkingItemizedOverlay itemizedoverlay = new ParkingItemizedOverlay(drawable, this);

		for(String key : parking.keySet()) {

			String[] l = parking.get(key);

			lattitudeValue = Double.parseDouble(l[1]); //converting string lattitude value to double
			longitudeValue=Double.parseDouble(l[0]); //converting string longitude value to double
			// System.out.println("point=" +lattitudeValue);
			GeoPoint point1 = new GeoPoint((int) (lattitudeValue * 1E6), (int) (longitudeValue * 1E6));
			OverlayItem overlayitem = new OverlayItem(point1, "", "");

			itemizedoverlay.addOverlay(overlayitem);
			mapOverlays.add(itemizedoverlay);
		}
	}

	private void displayAccesspt() {

		AccessptParse parseXMLTask = new AccessptParse(this);
		try {
			Greenway.greenways = parseXMLTask.execute("accesspt").get();
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

		for(String key : Greenway.greenways.keySet()) {

			String[] l = Greenway.greenways.get(key).getLocation();
			String title = Greenway.greenways.get(key).getTitle();
			String accesspt = Greenway.greenways.get(key).getAccesspt();

			lattitudeValue = Double.parseDouble(l[1]); //converting string lattitude value to double
			longitudeValue=Double.parseDouble(l[0]); //converting string longitude value to double
			// System.out.println("point=" +lattitudeValue);
			GeoPoint point1 = new GeoPoint((int) (lattitudeValue * 1E6), (int) (longitudeValue * 1E6));
			OverlayItem overlayitem = new OverlayItem(point1, title, accesspt);

			itemizedoverlay.addOverlay(overlayitem);
			mapOverlays.add(itemizedoverlay);
		}

	}


	public void updateWithNewLocation(Location location, int i) {

		if (location != null) {
			/*
    		Toast.makeText(
                        this,
                        "Current location:\nLatitude: " + location.getLatitude()
                                + "\n" + "Longitude: " + location.getLongitude(),
                        Toast.LENGTH_LONG).show();
			 */
			// Update the map location.
			Double geoLat = location.getLatitude()*1E6;
			Double geoLng = location.getLongitude()*1E6;
			GeoPoint point = new GeoPoint(geoLat.intValue(), geoLng.intValue());

			if(i == 0){
				mc.setCenter(point);
			}

			//mc.zoomToSpan(point.getLatitudeE6(),point.getLongitudeE6());
			mc.setZoom(17);


		} else {

			Toast.makeText(this, "Cannot fetch current location!",
					Toast.LENGTH_LONG).show();

		}

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
			Intent map = new Intent(GreenwayMap.this, GreenwayMap.class);     
			startActivity(map);
			break;
		case R.id.menu_weather:
			Weather.inital(this);
			break;
		case R.id.menu_citylink:
			Intent citylink = new Intent(GreenwayMap.this, CityLink.class);     
			startActivity(citylink);
			break;
		default:
			break;
		}
		return true;
	}*/


}
