package com.example.takemethere;

import java.util.List;
/**
 * Represents a Route entity.
 * As of now a route is for one floor only.
 * For multiple floor scenario, a set of routes will be used
 *
 */
public class Route {
	public int id;
	public Location startLocation;
	public Location endLocation;
	public List<Path> paths;
	
	public Route(int id, List<Path> paths, Location startLocation, Location endLocation){
		this.id = id;
		this.startLocation = startLocation;
		this.endLocation = endLocation;
		this.paths = paths;
	}
}
