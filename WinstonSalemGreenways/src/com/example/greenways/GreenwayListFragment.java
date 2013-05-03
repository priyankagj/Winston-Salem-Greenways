package com.example.greenways;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class GreenwayListFragment extends ListFragment {

	LocationManager locationManager;
	public static Location curLocation;
	
	// flag for GPS status
	public boolean isGPSEnabled = false;

	// flag for network status
	boolean isNetworkEnabled = false;

	// flag for GPS status
	boolean canGetLocation = false;

	LocationListener loclis = new LocationListener() {

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		public void onProviderEnabled(String provider) {
			curLocation = getCurrentLocation();

		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		public void onLocationChanged(Location location) {
			curLocation = getCurrentLocation();
		}
	};
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		curLocation = getCurrentLocation();

		/** The parsing of the xml data is done in a non-ui thread */
		GetDistanceParser listViewLoaderTask = new GetDistanceParser();

		Context context = this.getActivity();

		listViewLoaderTask.execute(context);		
	}

	private class GetDistanceParser extends AsyncTask<Context, Void, ArrayList<HashMap<String,String>>>{

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(Context... params) {
			// TODO Auto-generated method stub

			ArrayList<HashMap<String, String>> new_list = new ArrayList<HashMap<String,String>>();


			if(GreenwayLocation.greenways == null){ // For calling parser just once
				AccessptParse parseXMLTask = new AccessptParse(params[0]);
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

			URL url;
			InputStream io = null;
			HttpURLConnection connection = null;

			StringBuffer destinations = new StringBuffer();
			for(String key : GreenwayLocation.greenways.keySet()) {	
				String[] accessLocation = GreenwayLocation.greenways.get(key).getLocation();
				double latitudeValue = Double.parseDouble(accessLocation[1]); //converting string latitude value to double
				double longitudeValue = Double.parseDouble(accessLocation[0]);
				destinations.append(latitudeValue+","+longitudeValue+"|");
			}

			StringBuffer jSonUrl = new StringBuffer("http://maps.googleapis.com/maps/api/distancematrix/json?origins=");	
			jSonUrl.append(curLocation.getLatitude()+","+curLocation.getLongitude());

			jSonUrl.append("&destinations="+destinations);
			jSonUrl.append("&sensor=false&mode=driving&units=imperial");

			try {
				url = new URL(jSonUrl.toString());	
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.connect();
				
				while (io == null){
					io = connection.getInputStream();
				}

				final String result = convertStreamToString(io);

				ArrayList<String> distanceInMiles = new ArrayList<String>();
				try {
					JSONObject jsonObject = new JSONObject(result);
					System.out.println(result);

					JSONArray rows = jsonObject.getJSONArray("rows");
					Log.i("json", rows.toString());

					JSONArray elements = rows.getJSONObject(0).getJSONArray("elements");
					DecimalFormat df = new DecimalFormat("##0.00");
					for(int i=0;i<elements.length();i++){

						JSONObject ini_dis = elements.getJSONObject(i);
						JSONObject distance = ini_dis.getJSONObject("distance");		
						distanceInMiles.add(String.valueOf(df.format(distance.getDouble("value")/1609.34)));	
					}              
				} catch (JSONException e) {
					Log.e(e.getMessage(), "JsonParser - ");
				}

				//	DecimalFormat df = new DecimalFormat("##.##");
				int i=0;
				for(String key: GreenwayLocation.greenways.keySet()){
					String title = GreenwayLocation.greenways.get(key).getTitle();
					String accesspt = GreenwayLocation.greenways.get(key).getAccesspt();
					HashMap<String,String> temp = new HashMap<String,String>();
					temp.put("distance", distanceInMiles.get(i++)+" miles");
					temp.put("title", title);
					temp.put("accessPointName", accesspt);

					new_list.add(temp);
				}


			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				connection.disconnect();
			}

			Collections.sort(new_list, new Comparator<HashMap<String, String>>() {

				public int compare(HashMap<String, String> lhs,
						HashMap<String, String> rhs) {
					// TODO Auto-generated method stub
					Double i1 = Double.parseDouble(lhs.get("distance").split(" ")[0]);
					Double i2 = Double.parseDouble(rhs.get("distance").split(" ")[0]);
					return (i1<i2 ? -1 : (i1==i2 ? 0 : 1));
				}
			});
			locationManager.removeUpdates(loclis);
			return new_list;
		}

		protected void onPostExecute(ArrayList<HashMap<String, String>> list){
			SimpleAdapter adapter = new SimpleAdapter(
					getActivity(),
					list,
					R.layout.custome_row_view,
					new String[] {"title","accessPointName","distance"},
					new int[] {R.id.greenwayName,R.id.accessPointName, R.id.distance}
					);

			setListAdapter(adapter);

			ListView listView = getListView();
			listView.setCacheColorHint(0);
			listView.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> l, View v, int position, long id) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(), Greenway_Description.class);

					@SuppressWarnings("unchecked")
					HashMap<String, String> item = (HashMap<String, String>) l.getItemAtPosition(position);
					intent.putExtra("str", item.get("accessPointName"));
					startActivity(intent);
				}
			});

		}
	}

	/**
	 * Convert an inputstream to a string.
	 * @param input inputstream to convert.
	 * @return a String of the inputstream.
	 */

	private String convertStreamToString(final InputStream input) {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		final StringBuilder sBuf = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sBuf.append(line);
			}
		} catch (IOException e) {
			Log.e(e.getMessage(), "Google parser, stream2string");
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				Log.e(e.getMessage(), "Google parser, stream2string");
			}
		}
		return sBuf.toString();
	}

	/**
	 * Function to get the user's current location
	 * @return
	 */
	public Location getCurrentLocation() {
		Location location = null;
		try {
			locationManager = (LocationManager)this.getActivity().getSystemService(Context.LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			Log.v("isGPSEnabled", "=" + isGPSEnabled);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			Log.v("isNetworkEnabled", "=" + isNetworkEnabled);

			if (isGPSEnabled == false && isNetworkEnabled == false) {
				// no network provider is enabled
			} else {
				this.canGetLocation = true;
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							10, 1000*60*1, loclis);
					Log.d("Network", "Network");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						/*if (location != null) {
	                        latitude = location.getLatitude();
	                        longitude = location.getLongitude();
	                    }*/
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								10, 1000*60*1, loclis);
						Log.d("GPS Enabled", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							/*if (location != null) {
	                            latitude = location.getLatitude();
	                            longitude = location.getLongitude();
	                        }*/
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}
}



