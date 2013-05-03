package com.example.greenways;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

public class FrontLine extends Activity {
	
	//To check if the connection to the server is okay
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Put the following code for disabling window title after onCreate and before setContentView, else it won't work
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.frontline);

		new CountDownTimer(1000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {
				if(isNetworkAvailable())
				{		
					Intent intent=new Intent(FrontLine.this, GreenwayList.class);
					startActivity(intent);
				}
				else
				{
					Log.d("network error", "Oops.. unable to connect. Check your network settings!");
					showDialog();
					//Intent errorIntent = new Intent(FrontLine.this, NetworkError.class);
					//startActivity(errorIntent);
				}
			}
		}.start();


	}


	private void showDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(FrontLine.this);
		builder.setMessage("Enable Data Connection?")
		.setCancelable(false)
		.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
				startActivity(intent);                           
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@SuppressLint("ShowToast")
			public void onClick(DialogInterface dialog, int id) {
				Toast toast = Toast.makeText(getApplicationContext(), "Need Data connection", Toast.LENGTH_LONG);
				toast.show();
				finish();
				
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
}
