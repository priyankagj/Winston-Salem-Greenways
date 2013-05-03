package com.example.greenways;

import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class DirectionsButtonFragment extends Fragment{
	HashMap<String, GreenwayLocation> greenwayHashMap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.directionbutton, container, false);		
		greenwayHashMap = GreenwayLocation.greenways;

		String str = getActivity().getIntent().getStringExtra("str");
		String[] l = greenwayHashMap.get(str).getLocation();
		final double lattitudeValue = Double.parseDouble(l[1]); //converting string latitude value to double
		final double longitudeValue=Double.parseDouble(l[0]); //converting string longitude value to double

		Button buttonOne = (Button) view.findViewById(R.id.get_direction);
		buttonOne.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				final CharSequence[] items = {"Driving", "Walking", "Biking"};
				builder.setTitle("Choose your mode");

				builder.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						String mode = "d";
						if(items[item].equals("Walking"))
							mode = "w";
						else if(items[item].equals("Biking"))
							mode = "b";
						Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
								Uri.parse("http://maps.google.com/maps?daddr="+lattitudeValue+","+longitudeValue+
										"&saddr="+GreenwayListFragment.curLocation.getLatitude()+","
										+GreenwayListFragment.curLocation.getLongitude()
										+"&dirflg="+mode+"&view=map"));
						intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
						startActivity(intent);
					}
				});

				AlertDialog alert = builder.create();
				alert.show();
			}
		});

		return view;
	}
}
