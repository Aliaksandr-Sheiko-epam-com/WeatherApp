package com.epam.weatherapp.util;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.epam.weatherapp.model.LocationInfo;

public final class WeatherAdapter<T> extends ArrayAdapter<T> {
    private ArrayList<LocationInfo> locationList;

    //fixme
    public WeatherAdapter(Context context, int resource, T[] objects, ArrayList<LocationInfo> locationList) {
        super(context, resource, objects);
        this.locationList = locationList;
    }

    public LocationInfo getLocationInfo(int i) {
        return locationList.get(i);
    }
}
