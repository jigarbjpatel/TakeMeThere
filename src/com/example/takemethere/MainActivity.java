package com.example.takemethere;

import java.io.IOException;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Menu;
import android.widget.ImageView;

public class MainActivity extends Activity {

	public int stepCounter;
	public int angle;
	private SensorServiceReceiver sensorReceiverDirection;
	private SensorServiceReceiver sensorReceiverStep;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		startService(new Intent(this, SensorService.class));
		regsiterBroadCastReceivers();
		//crearPunto(0,0,200,200,Color.RED);
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
