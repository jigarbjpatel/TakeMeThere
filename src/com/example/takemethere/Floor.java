/**
 * 
 */
package com.example.takemethere;


/**
 * Represents the Floor entity
 */
public class Floor {
	public int id;
	public String name;
	public String mapPath;
	public int buildingId;
	
	public Floor(int id, String name, int buildingId, String mapPath){
		this.id = id;
		this.name = name;
		this.buildingId = buildingId;
		this.mapPath = mapPath;
	}
}
