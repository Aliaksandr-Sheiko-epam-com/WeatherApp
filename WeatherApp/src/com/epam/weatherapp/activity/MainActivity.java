package com.epam.weatherapp.activity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.epam.weatherapp.R;
import com.epam.weatherapp.util.WeatherAdapter;
import com.epam.weatherapp.util.pageloader.PageDisplayTask;
import com.epam.weatherapp.util.pageloader.PageLoadTask;

public final class MainActivity extends Activity {
	public final static String COUNTRY_KEY = "com.epam.weatherapp.activity.COUNTRY_KEY";
	private final static String URL_ADDRESS = "http://apidev.accuweather.com/locations/v1/cities/autocomplete?apikey=hAilspiKe&language=en&q=";
	private PageLoadTask dataLoader;
	private boolean isInternetAvailible;
	private AutoCompleteTextView textView;
	private NetworkReceiver receiver;
	private ExecutorService pool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		isInternetAvailible = checkConnection();
		textView = (AutoCompleteTextView) findViewById(R.id.auto_text_location);
		pool = Executors.newFixedThreadPool(1);
		tuneLocationView();
		createNetworkReceiver();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			this.unregisterReceiver(receiver);
		}
	}

	private void tuneLocationView() {
		addTextViewEmptyAdapter();
		textView.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (before != count) { // this condition for prevent calling page loader
										// twice when made only one change
					String searchLocation = textView.getText().toString();
					if (!searchLocation.isEmpty()) {
						if (dataLoader != null) {
							dataLoader.cancel();
						}
						if (isInternetAvailible) {
							String uriLocation = Uri.encode(searchLocation);
							dataLoader = new PageDisplayTask(textView,
									MainActivity.this, URL_ADDRESS
											+ uriLocation);
							pool.execute(dataLoader);
						} else {
							Toast.makeText(MainActivity.this,
									"Internet not availible",
									Toast.LENGTH_SHORT).show();
						}
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {/* NOP */}

			@Override
			public void afterTextChanged(Editable s) {/* NOP */}
		});

		textView.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				WeatherAdapter<String> myWeatherAdapter = (WeatherAdapter<String>) textView
						.getAdapter();
				callWeatherActivity(myWeatherAdapter.getLocationInfo(
						arg2).getKey());
			}
		});
	}
	
	private void callWeatherActivity(String locationKey) {
		Intent intent = new Intent(this, LocationWeatherActivity.class);
		intent.putExtra(COUNTRY_KEY, locationKey);
		startActivity(intent);
	}
	
	private void createNetworkReceiver() {
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		receiver = new NetworkReceiver();
		this.registerReceiver(receiver, filter);
	}

	private void addTextViewEmptyAdapter() { // need for start work dropdown
												// list at once
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, new String[] {});
		textView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private boolean checkConnection() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	public class NetworkReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (checkConnection()) {
				isInternetAvailible = true;
				Toast.makeText(context, "Internet availible",
						Toast.LENGTH_SHORT).show();
			} else {
				isInternetAvailible = false;
				Toast.makeText(context, "Internet not availible",
						Toast.LENGTH_SHORT).show();
			}
		}

	}
}
