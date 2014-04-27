package com.example.takemethere;

import java.util.List;

import com.example.takemethere.Location.LocationType;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MainActivity extends Activity {

	public int stepCounter;
	public int angle;
	private SensorServiceReceiver sensorReceiverDirection;
	private SensorServiceReceiver sensorReceiverStep;
	private static DBHelper dbHelper = null;
	private static Floor startFloor;
	private static Location startLocation;
	IntentResult scanResult;
	List<Location> locations;	
	ArrayAdapter<Location> adapter;
	Route route;
	int currPathIndex = 0;
	int remainingSteps = 0;
	public int avgStepLength = 5;
	Bitmap bmp = Bitmap.createBitmap(620, 320, Config.ARGB_8888);
	Canvas c ;
	ImageView imgMap ;
	private boolean serviceNotRunning = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		//TODO: Get initial sensor values and start tracking
	}
	@Override
	public void onResume(){   
		super.onResume();
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
		try{
			unregisterReceiver(sensorReceiverDirection);
			unregisterReceiver(sensorReceiverStep);
			serviceNotRunning = true;
		}catch(Exception ex){

		}
	}
	private void startService(){
		if(serviceNotRunning ){
			startService(new Intent(MainActivity.this, SensorService.class));
			regsiterBroadCastReceivers();
			serviceNotRunning = false;
		}


	}
	private void displayPossibleDestinations() {
		List<Location> possibleDestinations = dbHelper.getPossibleDestinations(startFloor.id);
		//TODO: Show destination list to user and when user selects return selected location
		ListView locationsListView = (ListView) findViewById(R.id.locationsListview);
		int resId = R.layout.locations_row_layout;
		adapter = new LocationsArrayAdapter(this,resId,possibleDestinations);
		locationsListView.setAdapter(adapter);
		MainActivity.LocationClickListener clickListener = new MainActivity.LocationClickListener();
		locationsListView.setOnItemClickListener(clickListener);
	}
	private Location getStartLocation() {
		// TODO Show QR Code screen and get input from the QR Code scanner
		//QR Code will give the location info.
		//Location startLocation = getLocationFromQRCode();
		startFloor = dbHelper.getFloor(startLocation.floorId);
		return startLocation;

	}
	private Location getLocationFromQRCode(int id, int floorId) {

		// TODO Auto-generated method stub
		Location l = new Location();
		l.id=id;
		l.floorId=floorId;
		l.name="Wean Cafe";
		l.type = LocationType.ENTRY;
		l.locationPoint = new Point();
		l.locationPoint.set(300, 227);
		return l;
	}


	public void init(){
		dbHelper = new DBHelper(this);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.close();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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

	public class SensorServiceReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if(intent.getAction().equals(SensorService.STEP_UPDATE))
				stepCounter = intent.getIntExtra(SensorService.STEPS, 0);
			else if(intent.getAction().equals(SensorService.DIRECTION_UPDATE))
				angle = intent.getIntExtra(SensorService.ANGLE,0);
			System.out.println(stepCounter + " " + angle);	
			updateGUI();			
		}

		private void updateGUI() {
			if(stepCounter >= remainingSteps){
				stepCounter = 0;
				currPathIndex++;
				//Toast.makeText(getApplicationContext(), String.valueOf(currPathIndex), Toast.LENGTH_SHORT);
				if(route.paths.size() == currPathIndex){
					Paint paint = new Paint();
					paint.setColor(Color.GREEN);
					paint.setStrokeWidth(5);
					Path path = route.paths.get(currPathIndex-1);
					c.drawLine(path.startPoint.x, path.startPoint.y,path.endPoint.x,path.endPoint.y, paint);
					imgMap.setImageBitmap(bmp);
					//Route Ended => Stop Tracking
					Toast.makeText(getApplicationContext(), "Hurray you have reached destination!", Toast.LENGTH_SHORT).show();
					//bmp = Bitmap.createBitmap(620, 320, Config.ARGB_8888);
					//c = new Canvas(bmp);
					//imgMap.draw(c);
					currPathIndex = 0;
					remainingSteps = 0;
				}else{
					try{
						remainingSteps = route.paths.get(currPathIndex).distance / avgStepLength;
						//Change path => update UI
						Paint paint = new Paint();
						paint.setColor(Color.GREEN);
						paint.setStrokeWidth(5);
						Path path = route.paths.get(currPathIndex-1);
						c.drawLine(path.startPoint.x, path.startPoint.y,path.endPoint.x,path.endPoint.y, paint);
						imgMap.setImageBitmap(bmp);	
						//imgMap.requestLayout();
						//imgMap.invalidate();
					}catch(Exception ex){
						Log.e("Sensor", ex.toString());
					}
				}
			}
		}
	}
	public class LocationClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View rowView, int position,long id) {
			@SuppressWarnings("unchecked")
			ArrayAdapter<Location> adapter = (ArrayAdapter<Location>) parent.getAdapter();
			Location endLocation = adapter.getItem(position);
			//Once destination is selected ,we will have start and end points => get the route using that
			route = dbHelper.getRoute(startLocation, endLocation);
			//Get the list of paths associated with the route draw the paths

			imgMap = (ImageView) findViewById(R.id.imgMap);
			imgMap.setVisibility(View.VISIBLE);

			ListView locationsList = (ListView) findViewById(R.id.locationsListview);
			locationsList.setVisibility(View.GONE);

			c = new Canvas(bmp);
			imgMap.draw(c);
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			paint.setStrokeWidth(5);
			for(Path path : route.paths){
				//drawPath(path);
				c.drawLine(path.startPoint.x, path.startPoint.y,path.endPoint.x,path.endPoint.y, paint);
			}
			imgMap.setImageBitmap(bmp);
			//Start Tracking
			currPathIndex = 0;
			remainingSteps = route.paths.get(currPathIndex).distance / avgStepLength;
			startService();
		}	
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		//if(item.getTitle()=="Scan"){
		IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
		integrator.initiateScan();
		//}
		return true;

	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanResult != null) {
			//System.out.println("Scan successful!");
			//String result = scanResult.toString();
			//String [] start = result.split(" ");
			//Location startLocation = getLocationFromQRCode(Integer.parseInt(start[1]), Integer.parseInt(start[2]));
			startLocation = getLocationFromQRCode(4,1);
			startFloor = dbHelper.getFloor(startLocation.floorId);
			displayPossibleDestinations();
		}
		else 
			System.out.println("Error in scanning");


	}

}
