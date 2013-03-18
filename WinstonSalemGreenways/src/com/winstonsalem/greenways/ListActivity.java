package com.winstonsalem.greenways;

import android.app.Activity;
import android.os.Bundle;

public class ListActivity extends Activity{
	 public void onCreate(Bundle savedInstanceState) {
	    	super.onCreate(savedInstanceState);
	    
	    	//Put the following code for disabling window title after onCreate and before setContentView, else it won't work
	        setContentView(R.layout.list_activity);
	 }

}
