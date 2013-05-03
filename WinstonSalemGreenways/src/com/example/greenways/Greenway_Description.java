package com.example.greenways;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.widget.TextView;

public class Greenway_Description extends FragmentActivity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.direction_desc);
        TextView v = (TextView) findViewById(android.R.id.title);
        v.setGravity(Gravity.CENTER);
        v.setTextSize(17);
	}
}