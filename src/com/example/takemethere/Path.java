package com.example.takemethere;

import android.graphics.Point;
/**
 * Represents Path entity.
 * Path can be between two points which are not considered as Location.
 * and hence it does not use Location entity.
 * A Path is on a single floor only as of now.
 */
public class Path {
	public int id;
	public Point startPoint;
	public Point endPoint;
	public int distance;
	public int floorId;
	public Path(int id, Point startPoint, Point endPoint, int distance, int floorId){
		this.id = id;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.distance = distance;
		this.floorId = floorId;
	}
}
