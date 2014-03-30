
package com.example.takemethere;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper class to access SQLite DB
 * @author Jigar
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "TakeMeThere.db";
	private static final int DATABASE_VERSION = 2;
	private static final String BUILDING_TABLE = "buildings";
	private static final String FLOOR_TABLE = "floors";
	private static final String LOCATION_TABLE = "locations";
	private static final String PATH_TABLE = "paths";
	private static final String ROUTE_TABLE = "routes";	
	private static final String ROUTE_PATHS_TABLE = "route_paths";
	/**
	 * SQL statement to create a new database and table
	 */
	private static final String CREATE_BUILDING_TABLE = 
			"create table " + BUILDING_TABLE + 
				" (buildingId integer primary key autoincrement, name text not null, owner text);";
	private static final String CREATE_FLOOR_TABLE  = " create table " + FLOOR_TABLE + 
				" (floorId integer primary key autoincrement, name text not null, mapPath text, buildingId integer not null);" ;
	
	private static final String CREATE_PATH_TABLE  = " create table " + PATH_TABLE + 
				" (pathId integer primary key autoincrement, floorId integer not null, distance integer, " +
				" startX integer not null, endX integer not null, startY integer not null, endY integer not null );" ;
	
	private static final String CREATE_LOCATION_TABLE  = " create table " + LOCATION_TABLE + 
				" (locationId integer primary key autoincrement, name text not null, locationType text," +
				" locationX integer not null, locationY integer not null, floorId integer not null);" ;
	private static final String CREATE_ROUTE_TABLE  = " create table " + ROUTE_TABLE + 
				" (routeId integer primary key autoincrement, " +
				" startX integer not null, endX integer not null, startY integer not null, endY integer not null);" ;
	private static final String CREATE_ROUTE_PATHS_TABLE  = " create table " + ROUTE_PATHS_TABLE + 
				" (routeId integer not null, pathId integer not null);" ;

	public DatabaseHelper(Context context, CursorFactory factory) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_BUILDING_TABLE);
		db.execSQL(CREATE_FLOOR_TABLE);
		db.execSQL(CREATE_PATH_TABLE);
		db.execSQL(CREATE_LOCATION_TABLE);
		db.execSQL(CREATE_ROUTE_TABLE);
		db.execSQL(CREATE_ROUTE_PATHS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + BUILDING_TABLE +";");
		db.execSQL("DROP TABLE IF EXISTS " + FLOOR_TABLE +";");
		db.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE +";");
		db.execSQL("DROP TABLE IF EXISTS " + PATH_TABLE +";");
		db.execSQL("DROP TABLE IF EXISTS " + ROUTE_TABLE +";");
		db.execSQL("DROP TABLE IF EXISTS " + ROUTE_PATHS_TABLE +";");
		onCreate(db);		
	}

}
