package com.example.greenways;

import java.util.HashMap;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DescriptionFragment extends Fragment{

	HashMap<String, GreenwayLocation> greenwayHashMap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.description, container, false);
		greenwayHashMap = GreenwayLocation.greenways;
		String str = getActivity().getIntent().getStringExtra("str");

		TextView nameGreenWay = (TextView) view.findViewById(R.id.nameGreenWay);
		nameGreenWay.setText(greenwayHashMap.get(str).getTitle()); 
		nameGreenWay.setTextColor(Color.argb(255, 00, 80, 00));

		TextView nameAccessPoint = (TextView) view.findViewById(R.id.nameAccessPoint);
		nameAccessPoint.setText("\n" + "Access Point at " +greenwayHashMap.get(str).getAccesspt()
				+ "\n" + "\n" +greenwayHashMap.get(str).getDescription());
		nameAccessPoint.setTextColor(Color.argb(255, 00, 00, 00));

		return view; 
	}
}
