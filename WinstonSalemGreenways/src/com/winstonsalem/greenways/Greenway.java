<<<<<<< HEAD
package com.winstonsalem.greenways;

public class Greenway {

	String greenway_id;
	GreenwayLocation[] points;
	int greenway_distance;
	String from;
	String to;
	
	public String getGreenway_id() {
		return greenway_id;
	}
	public void setGreenway_id(String greenway_id) {
		this.greenway_id = greenway_id;
	}
	public GreenwayLocation[] getPoints() {
		return points;
	}
	public void setPoints(GreenwayLocation[] points) {
		this.points = points;
	}
	public int getGreenway_distance() {
		return greenway_distance;
	}
	public void setGreenway_distance(int greenway_distance) {
		this.greenway_distance = greenway_distance;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}	
}
=======
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
>>>>>>> 06b29805e5318bfcdb184dccb7a0261ac2873509
