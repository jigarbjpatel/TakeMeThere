package com.example.takemethere;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DBHelper extends SQLiteAssetHelper {
	private static final String DATABASE_NAME = "TakeMeThere.db";
	private static final int DATABASE_VERSION = 5;
	private static final String BUILDING_TABLE = "buildings";
	private static final String FLOOR_TABLE = "floors";
	private static final String LOCATION_TABLE = "locations";
	private static final String PATH_TABLE = "paths";
	private static final String ROUTE_TABLE = "routes";	
	private static final String ROUTE_PATHS_TABLE = "route_paths";
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// you can use an alternate constructor to specify a database location 
		// (such as a folder on the sd card)		// you must ensure that this folder is available and you have permission
		// to write to it
		//super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);
		setForcedUpgrade();
	}
	public Floor getFloor(int floorId) {
		Floor f = null;
		try{
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.query(FLOOR_TABLE, 
					new String[] { "floorId", "name", "buildingId","mapPath"}, "floorId=?",
					new String[] { String.valueOf(floorId) }, null, null, null);

			if (cursor != null && cursor.moveToNext()){				
				f = new Floor(cursor.getInt(0),
						cursor.getString(1),
						cursor.getInt(2),
						cursor.getString(3));
				cursor.close();			
			}
			db.close();
		}catch(Exception e){
			System.err.println(e);
		}
		return f;
	}

	public List<Location> getPossibleDestinations(int floorId) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(LOCATION_TABLE, 
				new String[] { "floorId", "locationId", "name","locationX", "locationY", "locationType"}, "floorId=?",
				new String[] { String.valueOf(floorId) }, null, null, null);
		List<Location> locations = new ArrayList<Location>();
		if (cursor != null){
			cursor.moveToFirst();
			do{
				Location l = new Location();
				l.id = cursor.getInt(1);					
				l.name = cursor.getString(2);
				l.floorId = cursor.getInt(0);
				try{
					l.type = Enum.valueOf(Location.LocationType.class, cursor.getString(5));
				}catch(IllegalArgumentException e){
					System.err.println(e);
				}
				l.locationPoint = new Point();
				l.locationPoint.set(cursor.getInt(3),cursor.getInt(4));
				locations.add(l);
			}while(cursor.moveToNext());
			cursor.close();			
		}
		db.close();
		return locations;
	}

	public Route getRoute(Location startLocation, Location endLocation) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(ROUTE_TABLE, 
				new String[] { "routeId", "startLocationId", "endLocationId"}, 
				" startLocationId=? AND endLocationId=? ",
				new String[] { String.valueOf(startLocation.id),String.valueOf(endLocation.id)}, 
				null, null, null);
		Route r = null;
		if(cursor.moveToFirst()){			
			r = new Route(cursor.getInt(0),startLocation,endLocation);
			r.paths = getPaths(r.id);
			cursor.close();
		}
		db.close();
		return r;
	}

	private List<Path> getPaths(int routeId) {
		List<Path> paths = new ArrayList<Path>();
		SQLiteDatabase db = this.getReadableDatabase();
		String query = "SELECT pathId, floorId, distance, startX, startY, endX, endY" +
				" FROM " + PATH_TABLE +" WHERE pathId IN " +
				" (SELECT pathId FROM " + ROUTE_PATHS_TABLE + " WHERE routeId = " + routeId +");";
		Cursor cursor = db.rawQuery(query, null);
		if(cursor.moveToFirst()){			
			do{
				Path p = new Path(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2));
				p.startPoint = new Point();
				p.startPoint.set(cursor.getInt(3), cursor.getInt(4));
				p.endPoint = new Point();
				p.endPoint.set(cursor.getInt(5), cursor.getInt(6));
				paths.add(p);
			}while(cursor.moveToNext());
			cursor.close();
		}
		db.close();
		return paths;
	}
}
