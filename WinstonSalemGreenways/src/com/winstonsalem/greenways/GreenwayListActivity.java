package com.winstonsalem.greenways;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class GreenwayListActivity extends ListActivity{
	private ArrayList<HashMap<String,String>> list = 
			new ArrayList<HashMap<String,String>>(); 
	LocationManager locationManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_activity);

		ButtonPanel btn = (ButtonPanel) findViewById(R.id.buttonPanel);
		btn.initHeader(this);

		final ListView lv = getListView();
		SimpleAdapter adapter = new SimpleAdapter(
				this,
				list,
				R.layout.custom_row_view,
				new String[] {"title","accessPointName","distance"},
				new int[] {R.id.greenwayName,R.id.accessPointName, R.id.distance}
				);
		populateList();
		setListAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(GreenwayListActivity.this, Greenway_Description.class);

				@SuppressWarnings("unchecked")
				HashMap<String, String> item = (HashMap<String, String>) lv.getItemAtPosition(position);
				intent.putExtra("str", item.get("accessPointName"));
				System.out.println(item.get("accessPointName"));
				startActivity(intent);
			}			
		});
	}

	/**
	 * Populate the array list using the hashmap greenways.
	 */
	private void populateList() {

		if(Greenway.greenways == null){ // For calling parser just once
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
		}

		Location curLocation = getCurrentLocation();
		/*Location curLocation = new Location("dummy");
		curLocation.setLatitude(36.160642);
		curLocation.setLongitude(-80.305375);*/

		for(String key : Greenway.greenways.keySet()) {

			String[] accessLocation = Greenway.greenways.get(key).getLocation();
			String title = Greenway.greenways.get(key).getTitle();
			String accesspt = Greenway.greenways.get(key).getAccesspt();

			double latitudeValue = Double.parseDouble(accessLocation[1]); //converting string latitude value to double
			double longitudeValue = Double.parseDouble(accessLocation[0]); //converting string longitude value to double

			double distanceDouble = getDistance(latitudeValue, longitudeValue, curLocation.getLatitude(), curLocation.getLongitude());

			DecimalFormat df = new DecimalFormat("##.##");
			Double distanceInMiles = distanceDouble*0.000621371;
			distanceInMiles = Double.valueOf(df.format(distanceInMiles));
			final String distanceString = String.valueOf(distanceInMiles);    

			HashMap<String,String> temp = new HashMap<String,String>();
			temp.put("distance", distanceString+" miles");
			temp.put("title", title);
			temp.put("accessPointName", accesspt);

			list.add(temp);			
		}

		Collections.sort(list, new Comparator<HashMap<String, String>>() {

			public int compare(HashMap<String, String> lhs,
					HashMap<String, String> rhs) {
				// TODO Auto-generated method stub
				return lhs.get("distance").compareTo(rhs.get("distance"));
			}
		});
	}

	/**
	 * To fetch the current location of the user
	 * @return The location object
	 */

	private Location getCurrentLocation() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);

		String provider = locationManager.getBestProvider(criteria, true);
		Location location = locationManager.getLastKnownLocation(provider);
		LocationListener loclis = new LocationListener() {

			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub

			}

			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				updateWithNewLocation(location);
			}
		};

		locationManager.requestLocationUpdates(provider, 2000, 10, loclis); 
		return location;
	}

	private void updateWithNewLocation(Location location) {
		// TODO Auto-generated method stub
		/*double lat = location.getLatitude();
		double lng = location.getLongitude();*/
	}

	/**
	 * Finds distance between two coordinate pairs.
	 *
	 * @param lat1 First latitude in degrees
	 * @param lon1 First longitude in degrees
	 * @param lat2 Second latitude in degrees
	 * @param lon2 Second longitude in degrees
	 * @return distance in meters
	 */
	public static double getDistance(double lat1, double lon1, double lat2, double lon2) {

		final double Radius = 6371 * 1E3; // Earth's mean radius

		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return Radius * c;
	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}

	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case R.id.menu_map:
			Intent map = new Intent(GreenwayListActivity.this, GreenwayMap.class);     
			startActivity(map);
			break;
		case R.id.menu_weather:
			Weather.inital(this);
			break;
		case R.id.menu_citylink:
			Intent citylink = new Intent(GreenwayListActivity.this, CityLink.class);     
			startActivity(citylink);
			break;
		default:
			break;
		}
		return true;
	}*/
}
