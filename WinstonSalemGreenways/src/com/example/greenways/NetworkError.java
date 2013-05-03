package com.example.greenways;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class NetworkError extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("first", "hello all izz well");
		//Put the following code for disabling window title after onCreate and before setContentView, else it won't work
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.networkerror);
	}

}

