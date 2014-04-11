package com.example.takemethere;

import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LocationsArrayAdapter extends ArrayAdapter<Location> {
	private Context context;
	private List<Location> locations;
	int resourceId;
	public LocationsArrayAdapter(Context context, int resourceId,
			List<Location> locations) {
		super(context, resourceId, locations);
		//super(context,R.layout.items_row_layout,list);
		this.locations = locations;
		this.resourceId = resourceId;
		this.context = context;
	}
	@Override
	public View  getView (int position, View convertView, ViewGroup parent) {
	    if (convertView == null) {
	        //This a new view, hence we inflate the new layout
	        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        convertView = inflater.inflate(resourceId, parent, false);
	    }
		Location l = locations.get(position);
		TextView locationName = (TextView) convertView.findViewById(R.id.locationsName);
		locationName.setText(l.name);
		//convertView.setTag(i.getId());
		return convertView;
	}
}
