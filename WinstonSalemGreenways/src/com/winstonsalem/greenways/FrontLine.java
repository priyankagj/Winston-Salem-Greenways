package com.winstonsalem.greenways;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Window;

public class FrontLine extends Activity {
    /** Called when the activity is first created. */
	
	//To check if the connection to the server is ok
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
        setContentView(R.layout.activity_front_line);
      
        new CountDownTimer(1000, 1000) {

		     @Override
			public void onTick(long millisUntilFinished) {
		     }

		     @Override
			public void onFinish() {
		 		if(isNetworkAvailable())
		 		{		 	
		 			Intent intent=new Intent(FrontLine.this, ListActivity.class);
		 			startActivity(intent);
		 		}
		 		else
		 		{
		 			Log.d("network error", "Oops.. unable to connect. Check your network settings!");
		 	    	Intent errorIntent = new Intent(FrontLine.this, NetworkError.class);
 		        	startActivity(errorIntent);
		 		}
		     }
		  }.start();
        
		
    }
}