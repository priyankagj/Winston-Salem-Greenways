package com.winstonsalem.greenways;

import java.util.HashMap;

import android.os.Parcel;
import android.os.Parcelable;

//import android.location.Location;

public class Greenway{
	private String title;
	private String[] location;
	public static HashMap<String, Greenway> greenways;
	
	public Greenway() { greenways = new HashMap<String, Greenway>(); }
	/*
	private Greenway(Parcel in) {
		int size = in.readInt();
		  for(int i = 0; i < size; i++){
		    String key = in.readString();
		    Greenway value = (Greenway)in.readValue(Greenway.class.getClassLoader());
		    greenways.put(key, value);
		  }
	}
	*/
	/*private void readFromParcel(Parcel in) {
		// TODO Auto-generated method stub
		greenways = in.readHashMap(greenways, Greenway.class.getClassLoader());
	}*/

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
	/*
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(greenways.size());
		  for(String key : greenways.keySet()){
		    dest.writeString(key);
		    dest.writeValue(greenways.get(key));
		  }
	}
	
	 public static final Parcelable.Creator<Greenway> CREATOR =
		    	new Parcelable.Creator<Greenway>() {
		            public Greenway createFromParcel(Parcel in) {
		                return new Greenway(in);
		            }
		 
		            public Greenway[] newArray(int size) {
		                return new Greenway[size];
		            }
		        };*/
}
