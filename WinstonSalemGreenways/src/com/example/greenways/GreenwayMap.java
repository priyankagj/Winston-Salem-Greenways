package com.example.greenways;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MyLocationOverlay;

public class GreenwayMap extends FragmentActivity implements OnInfoWindowClickListener, OnMyLocationChangeListener{
	private GoogleMap mMap;
	private UiSettings mapUI;
	MapController mc;
	GeoPoint p;
	ArrayList<Marker> markers = new ArrayList<Marker>();

	double lattitudeValue;
	double longitudeValue;

	double lattitudeValue2;
	double longitudeValue2;

	String provider;
	MyLocationOverlay myLocationOverlay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.greenway_map);
        TextView v = (TextView) findViewById(android.R.id.title);
        v.setGravity(Gravity.CENTER);
        v.setTextSize(17);
		setUpMapIfNeeded();	

	}

	//Setting up the map at the startup
	private void setUpMapIfNeeded() {
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}

	}

	//All the things displayed within map
	private void setUpMap() {

		//listener for access points marker
		mMap.setOnInfoWindowClickListener(this);

		//listener for access points marker
		mMap.setOnMyLocationChangeListener(this);

		mapUI = mMap.getUiSettings();
		mapUI.setZoomControlsEnabled(true);
		mMap.setMyLocationEnabled(true);

		LatLng latLng = new LatLng(GreenwayListFragment.curLocation.getLatitude(), GreenwayListFragment.curLocation.getLongitude());
		//mMap.addMarker(new MarkerOptions().position(latLng)
		//		.icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation)));
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,15);
		mMap.animateCamera(cameraUpdate);

		//Parse accesspt kml and show the coordinates as Markers in the map
		displayAccesspt();

		//Parse parkinglot kml and show the coordinates as Markers in the map
		displayParkingArea();

		//Parse greenways kml and show the coordinates as a path drawn for each greenway in the map
		displayGreenway();

	}

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

		Iterator<String[]> itr = line.iterator();
		String[] l = new String[3];
		String[] l2 = new String[3];

		int count = 0;
		int itrcount = 0;

		//Iterating through all the coordinates in the kml and drawing a polyline for each greenway 
		while(itr.hasNext()){	
			itrcount++;
			String[] ls = itr.next();
			count += ls.length;
			PolylineOptions rectOptions = null;
			final int ROUTECOLOR = 0x7F008000;
			final float ROUTEWIDTH = 7.0f;
			if(ls.length > 10){
				for(int i=0;i<ls.length-6;i=i+6){
					l = ls[i].split(",");
					lattitudeValue = Double.parseDouble(l[1]); //converting string latitude value to double
					longitudeValue=Double.parseDouble(l[0]); //converting string longitude value to double

					if(i+6 > ls.length-2){
						l2 = ls[ls.length-2].split(",");
					}
					else{
						l2 = ls[i+6].split(",");
					}
					lattitudeValue2 = Double.parseDouble(l2[1]); //converting string latitude value to double
					longitudeValue2 =Double.parseDouble(l2[0]); //converting string longitude value to double

					rectOptions = new PolylineOptions().add(new LatLng(lattitudeValue, longitudeValue)).
							add(new LatLng(lattitudeValue2,longitudeValue2)).color(ROUTECOLOR).width(ROUTEWIDTH);
					mMap.addPolyline(rectOptions);
				}
			}
			else{
				for(int i=0;i<ls.length-4;i=i+3){
					l = ls[i].split(",");
					lattitudeValue = Double.parseDouble(l[1]); //converting string lattitude value to double
					longitudeValue=Double.parseDouble(l[0]); //converting string longitude value to double

					if(i+3 > ls.length-2){
						l2 = ls[ls.length-2].split(",");
					}
					else{
						l2 = ls[i+3].split(",");
					}
					lattitudeValue2 = Double.parseDouble(l2[1]); //converting string lattitude value to double
					longitudeValue2 =Double.parseDouble(l2[0]); //converting string longitude value to double

					rectOptions = new PolylineOptions().add(new LatLng(lattitudeValue, longitudeValue)).
							add(new LatLng(lattitudeValue2,longitudeValue2)).color(ROUTECOLOR).width(ROUTEWIDTH);
					mMap.addPolyline(rectOptions);				
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

		//Iterating through all the parking spots in the kml and displaying as a marker
		for(String key : parking.keySet()) {

			String[] l = parking.get(key);

			lattitudeValue = Double.parseDouble(l[1]); //converting string lattitude value to double
			longitudeValue=Double.parseDouble(l[0]); //converting string longitude value to double

			mMap.addMarker(new MarkerOptions()
			.position(new LatLng(lattitudeValue, longitudeValue)).
			icon(BitmapDescriptorFactory.fromResource(R.drawable.parkingmarker)));
		}

	}


	private void displayAccesspt() {

		if(GreenwayLocation.greenways == null){ // For calling parser just once
			AccessptParse parseXMLTask = new AccessptParse(this);
			try {
				GreenwayLocation.greenways = parseXMLTask.execute("accesspt").get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//Iterating through all the access points in the kml and displaying as a marker
		for(String key : GreenwayLocation.greenways.keySet()) {

			String[] l = GreenwayLocation.greenways.get(key).getLocation();
			String title = GreenwayLocation.greenways.get(key).getTitle();
			String accesspt = GreenwayLocation.greenways.get(key).getAccesspt();

			lattitudeValue = Double.parseDouble(l[1]); //converting string lattitude value to double
			longitudeValue=Double.parseDouble(l[0]); //converting string longitude value to double

			markers.add(mMap.addMarker(new MarkerOptions()
			.position(new LatLng(lattitudeValue, longitudeValue)).
			icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).
			title(title).snippet(accesspt)));

		}

	}

	//clickable event on title window of the access point
	public void onInfoWindowClick(Marker marker) {
		System.out.println("Here");
		for(int i=0; i<markers.size();i++){
			if (marker.equals(markers.get(i))) 
			{
				System.out.println("There");
				Intent intent=new Intent(this, Greenway_Description.class);
				intent.putExtra("str", marker.getSnippet());		
				startActivity(intent);

			}
		}

	}

	public void onMyLocationChange(Location location) {

		GreenwayListFragment.curLocation = location;

	}

}
