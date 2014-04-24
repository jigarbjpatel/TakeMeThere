package com.example.takemethere;

import android.graphics.Point;
/**
 * Represents Path entity.
 * Path can be between two points which are not considered as Location.
 * and hence it does not use Location entity.
 * Path ends when there is a decision to be made in choosing a way.
 * A Path is on a single floor only as of now.
 */
public class Path {
	public int id;
	public Point startPoint;
	public Point endPoint;
	public int distance;
	public int floorId;
	//TODO: Use SubPaths which help in displaying detailed tracking. 
	//Use number of steps in a path and number of points available to update the UI
	//public Point[] subPaths;
	public Path(int id, int floorId, int distance){
		this.id = id;
		this.distance = distance;
		this.floorId = floorId;
	}
	public Path(int id, Point startPoint, Point endPoint, int distance, int floorId){
		this.id = id;
		this.distance = distance;
		this.floorId = floorId;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}
}
