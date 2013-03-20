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
