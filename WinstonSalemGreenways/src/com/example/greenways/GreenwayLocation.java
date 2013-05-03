package com.example.greenways;

import java.util.HashMap;

//import android.location.Location;

public class GreenwayLocation{
	private String title;
	private String accesspt;
	private String[] location;
	public static HashMap<String, GreenwayLocation> greenways;
	private String description;
	
	public GreenwayLocation() { greenways = new HashMap<String, GreenwayLocation>(); }
	
	public void setTitle(String t){
		title = t;
	}
	
	public void setLocation(String[] t){
		location = t;
	}
	
	public void setAccessPt(String t){
		accesspt = t;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String[] getLocation(){
		return location;
	}
	
	public String getAccesspt(){
		return accesspt;
	}
	
	public void setDescription(String desc){
		description = desc;
	}
	
	public String getDescription(){
		return description;
	}
}

