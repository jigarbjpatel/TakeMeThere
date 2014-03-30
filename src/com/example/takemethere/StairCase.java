package com.example.takemethere;

import java.util.List;

public class StairCase {
	public int id;
	public int buildingId;
	public List<Integer> floorIds;
	
	public StairCase(int id, int buildingId, List<Integer> floorIds){
		this.id = id;
		this.floorIds = floorIds;
		this.buildingId = buildingId;
	}
}
