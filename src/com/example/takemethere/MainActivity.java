package com.example.takemethere;

import java.util.List;

import com.example.takemethere.Location.LocationType;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

	public int stepCounter;
	public int angle;
	private SensorServiceReceiver sensorReceiverDirection;
	private SensorServiceReceiver sensorReceiverStep;
	private static DatabaseHelper dbHelper = null;
	private static Floor startFloor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Show the input screen to get start location
		Location startLocation = getStartLocation();
		//Once start location is identified, show the screen with list of locations in that floor
		Location endLocation = getEndLocation();
		//Once destination is selected ,we will have start and end points => get the route using that
		Route route = getRoute(startLocation,endLocation);
		//Get the list of paths associated with the route draw the paths
		for(Path path : route.paths){
			drawPath(path);
		}
		//TODO: Get initial sensor values and start tracking
		
		/*startService(new Intent(this, SensorService.class));
		regsiterBroadCastReceivers();*/
	}
	private void drawPath(Path path) {
		// TODO Auto-generated method stub
		
	}
	private Route getRoute(Location startLocation, Location endLocation) {
		return dbHelper.getRoute(startLocation, endLocation);
	}
	private Location getEndLocation() {
		List<Location> possibleDestinations = dbHelper.getPossibleDestinations(startFloor.id);
		//TODO: Show destination list to user and when user selects return
		
		Location endLocation = null;
		return endLocation;
	}
	private Location getStartLocation() {
		// TODO Show QR Code screen and get input from the QR Code scanner
		//QR Code will give the location info.
		Location startLocation = getLocationFromQRCode();
		startFloor = dbHelper.getFloor(startLocation.floorId);
		return startLocation;
	}
	private Location getLocationFromQRCode() {
		// TODO Auto-generated method stub
		Location l = new Location();
		l.id=1;
		l.floorId=5;
		l.name="Wean Cafe";
		l.type = LocationType.ENTRY;
		l.locationPoint = new Point();
		l.locationPoint.set(10, 10);
		return l;
	}
	public void init(){
		dbHelper = new DatabaseHelper(this,null);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.close();
	}
	/*private void crearPunto(float x, float y, float xend, float yend, int color) {
		ImageView imgMap = (ImageView) findViewById(R.id.imgMap);
		//System.out.println(imgMap.getWidth());
	    Bitmap bmp = Bitmap.createBitmap(500, 900, Config.ARGB_8888);
	    Canvas c = new Canvas(bmp);
	    imgMap.draw(c);

	    Paint p = new Paint();
	    p.setColor(color);
	    c.drawLine(x, y, xend, yend, p);
	    imgMap.setImageBitmap(bmp);
	}*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public void onResume(){   
		super.onResume();
		init();
	}
	/**
	 * Creates and registers two intent filters - for direction and steps update 
	 */
	private void regsiterBroadCastReceivers() {
		IntentFilter directionFilter = new IntentFilter(SensorService.DIRECTION_UPDATE);
		sensorReceiverDirection = new SensorServiceReceiver();
		registerReceiver(sensorReceiverDirection, directionFilter);
		IntentFilter stepsFilter = new IntentFilter(SensorService.STEP_UPDATE);
        sensorReceiverStep = new SensorServiceReceiver();
        registerReceiver(sensorReceiverStep, stepsFilter);
	}

	@Override
	public void onStop(){
		super.onStop();
		System.out.println("Stop");
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
		System.out.println("Destroy");
		unregisterReceiver(sensorReceiverDirection);
		unregisterReceiver(sensorReceiverStep);
	}
	public class SensorServiceReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if(intent.getAction().equals(SensorService.STEP_UPDATE))
				stepCounter = intent.getIntExtra(SensorService.STEPS, 0);
			else if(intent.getAction().equals(SensorService.DIRECTION_UPDATE))
				angle = intent.getIntExtra(SensorService.ANGLE,0);
			System.out.println(stepCounter + " " + angle);	
			//updateGUI();			
		}
	}
}
