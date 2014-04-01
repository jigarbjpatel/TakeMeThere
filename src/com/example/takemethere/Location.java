package com.example.takemethere;

import android.graphics.Point;

/**
 * Represents the Location (Destination/Landmark) entity
 */
public class Location {
	public enum LocationType {
		ENTRY,
		EXIT,
		ELEVATOR,
		STAIRCASE,
		OTHER
	}
	public int id;
	public String name;
	public Point locationPoint;
	//Used for representing elevtors/staircases and other special locations like entry/exit
	public LocationType type;
	public int floorId;
	public Location(int id, String name, Point locationPoint, int floorId, LocationType type){
		this.id = id;
		this.name = name;
		this.locationPoint = locationPoint;
		this.floorId = floorId;
		this.type = type;
	}
	public Location() {
		// TODO Auto-generated constructor stub
	}
}
