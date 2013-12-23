package com.epam.weatherapp.util.pageloader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.epam.weatherapp.exception.ReadWebPageException;
import com.epam.weatherapp.exception.WeatherParseException;
import com.epam.weatherapp.model.LocationInfo;
import com.epam.weatherapp.util.LocationInfoParser;
import com.epam.weatherapp.util.WeatherAdapter;

public final class AvailableLocationDisplayTask extends WebPageLoadTask {
    private final static String EXCEPTION_TAG = "AvailableLocationDisplay";
    private AutoCompleteTextView textView;
    private Activity activity;

    public AvailableLocationDisplayTask(AutoCompleteTextView textView, Activity activity, String url) {
        super(url);
        this.textView = textView;
        this.activity = activity;
    }

    public AvailableLocationDisplayTask(AutoCompleteTextView textView, Activity activity, String url,
        IPageDownloader pageDownloader) {
        super(url, pageDownloader);
        this.textView = textView;
        this.activity = activity;
    }

    @Override
    protected void onSuccessPostExecute(final String result) {
        try {
            final ArrayList<LocationInfo> locationList = LocationInfoParser.getLocationList(result);

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    //fixme
                    ArrayAdapter<String> adapter;
                    if (locationList.size() == 0) {
                        adapter = new WeatherAdapter<String>(activity, android.R.layout.simple_list_item_1, new String[] {},
                            locationList);
                    }
                    else {
                        adapter = new WeatherAdapter<String>(activity, android.R.layout.simple_list_item_1,
                            createLocationArray(locationList), locationList);
                    }
                    textView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            });
        }
        catch (WeatherParseException e) {
            Toast.makeText(activity, "Bad data were received", Toast.LENGTH_SHORT).show();
            Log.e(EXCEPTION_TAG, "Bad data were received", e);
        }
    }

    @Override
    protected void onFailPostExecute(ReadWebPageException readException) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(activity, "Problem with data receiving occured", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String[] createLocationArray(List<LocationInfo> locationList) {
        //fixme
        Iterator<LocationInfo> locationIterator = locationList.iterator();
        String[] locationNameArray = new String[locationList.size()];
        LocationInfo location;
        int i = 0;
        while (locationIterator.hasNext()) {
            location = locationIterator.next();
            locationNameArray[i++] = resultItem(location);
        }
        return locationNameArray;
    }

    private String resultItem(LocationInfo location) {
        return location.getCityName() + " : " + location.getAdministativeAreaName() + " : " + location.getCountryName();
    }
}
