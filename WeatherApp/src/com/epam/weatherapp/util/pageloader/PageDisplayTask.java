package com.epam.weatherapp.util.pageloader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.epam.weatherapp.exception.TechnicException;
import com.epam.weatherapp.util.AccuweatherInfoParser;
import com.epam.weatherapp.util.Constant;
import com.epam.weatherapp.util.WeatherAdapter;

public final class PageDisplayTask extends PageLoadTask {
	private AutoCompleteTextView textView;
	private Activity activity;

	public PageDisplayTask(AutoCompleteTextView textView, Activity activity,
			String url) {
		super(url);
		this.textView = textView;
		this.activity = activity;
	}

	public PageDisplayTask(AutoCompleteTextView textView, Activity activity,
			String url, IPageDownloader pageDownloader) {
		super(url, pageDownloader);
		this.textView = textView;
		this.activity = activity;
	}

	@Override
	protected void onPostExecute(final String result) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				try {
					ArrayList<AccuweatherInfoParser.LocationInfo> locationList = AccuweatherInfoParser
							.getLocationList(result);
					locationList.toArray();
					ArrayAdapter<String> adapter;
					if (locationList.size() == 0) {
						adapter = new WeatherAdapter<String>(activity,
								android.R.layout.simple_list_item_1,
								new String[] {}, locationList);
						Log.i(Constant.INFO_TAG, "Empty");
					} else {
						adapter = new WeatherAdapter<String>(activity,
								android.R.layout.simple_list_item_1,
								createLocationArray(locationList), locationList);
						Log.i(Constant.INFO_TAG, "Not empty");
					}
					//textView.set
					textView.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				} catch (TechnicException e) {
					Toast.makeText(activity, "Bad data were received",
							Toast.LENGTH_SHORT).show();
					Log.e(Constant.EXCEPTION_TAG,
							"Bad data were received");
				}
			}
		});
	}

	private String[] createLocationArray(
			List<AccuweatherInfoParser.LocationInfo> locationList) {
		Iterator<AccuweatherInfoParser.LocationInfo> locationIterator = locationList
				.iterator();
		String[] locationNameArray = new String[locationList.size()];
		AccuweatherInfoParser.LocationInfo location;
		int i = 0;
		while (locationIterator.hasNext()) {
			location = locationIterator.next();
			locationNameArray[i++] = resultItem(location);
		}
		return locationNameArray;
	}

	private String resultItem(AccuweatherInfoParser.LocationInfo location) {
		return location.getCityName() + " : "
				+ location.getAdministativeAreaName() + " : "
				+ location.getCountryName();
	}
}
