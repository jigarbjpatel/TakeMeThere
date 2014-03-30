package com.example.takemethere;

/**
 * Represents the Building entity.
 */
public class Building {
	public int id;
	public String name;
	public String owner;
	public Building(int id, String name, String owner){
		this.id = id;
		this.name = name;
		this.owner = owner;
	}
}
