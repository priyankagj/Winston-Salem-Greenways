package com.winstonsalem.greenways;

//import android.location.Location;

public class Greenway {
	private String title;
	private String[] location;
	
	public void setTitle(String t){
		title = t;
	}
	
	public void setLocation(String[] t){
		location = t;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String[] getLocation(){
		return location;
	}
}
