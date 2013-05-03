package com.example.greenways;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class Header extends Fragment {

	Button homeButton;
	Button mapButton;
	Button weatherButton;
	Button citilink;
	ProgressDialog myPd_ring = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.header, container, false);

		homeButton = (Button) view.findViewById(R.id.home);
		mapButton = (Button) view.findViewById(R.id.map);
		weatherButton = (Button) view.findViewById(R.id.weather);
		citilink = (Button) view.findViewById(R.id.citilink);

		final Activity activity = getActivity();

		homeButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				myPd_ring = ProgressDialog.show(activity, "", "Loading please wait..", true);
				myPd_ring.setCancelable(true);
				new Thread(new Runnable() {
					public void run() {

						Intent intent = new Intent(activity, GreenwayList.class);
						//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						activity.startActivity(intent);
						//activity.finish();
						try {
							Thread.sleep(7000);
						} catch (Exception e) {
						}
						myPd_ring.dismiss();
					}
					
				}).start();
				
			}

		});

		mapButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				myPd_ring = ProgressDialog.show(activity,
						"", "Loading please wait..", true);
				myPd_ring.setCancelable(true);
				new Thread(new Runnable() {
					public void run() {

						Intent intent = new Intent(activity, GreenwayMap.class);
						
						activity.startActivity(intent);
						try {
							Thread.sleep(10000);
						} catch (Exception e) {
						}
						myPd_ring.dismiss();
						//Header.this.handler.sendEmptyMessage(0);
					}
				}).start();

			}

		});

		weatherButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				myPd_ring = ProgressDialog.show(activity,
						"", "Loading please wait..", true);
				myPd_ring.setCancelable(true);
				new Thread(new Runnable() {
					public void run() {

						Intent intent = new Intent(activity, Weather.class);
						activity.startActivity(intent);
						try {
							Thread.sleep(5000);
						} catch (Exception e) {
						}
						myPd_ring.dismiss();
						//Header.this.handler.sendEmptyMessage(0);
					}
				}).start();

			}

		});

		citilink.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				myPd_ring = ProgressDialog.show(activity,
						"", "Loading please wait..", true);
				myPd_ring.setCancelable(true);
				new Thread(new Runnable() {
					public void run() {
						Intent intent = new Intent(activity, CityLink.class);

						activity.startActivity(intent);
						try {
							Thread.sleep(10000);
						} catch (Exception e) {
						}
						myPd_ring.dismiss();
						//Header.this.handler.sendEmptyMessage(0);
					}
				}).start();

			}

		});

		return view;
	}

}