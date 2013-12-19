package com.epam.weatherapp.util;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ArrayAdapter;

public final class WeatherAdapter<T> extends ArrayAdapter<T> {
	private ArrayList<AccuweatherInfoParser.LocationInfo> locationList;

	public WeatherAdapter(Context context, int resource, T[] objects,
			ArrayList<AccuweatherInfoParser.LocationInfo> locationList) {
		super(context, resource, objects);
		this.locationList = locationList;
	}

	public AccuweatherInfoParser.LocationInfo getLocationInfo(int i) {
		return locationList.get(i);
	}

}
