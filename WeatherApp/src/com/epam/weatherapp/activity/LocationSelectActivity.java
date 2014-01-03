package com.epam.weatherapp.activity;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.epam.weatherapp.R;
import com.epam.weatherapp.fragment.LocationWeatherFragment;
import com.epam.weatherapp.model.LocationInfo;
import com.epam.weatherapp.util.dataviewer.DisplayAvailableLocationTask;
import com.epam.weatherapp.util.pageloader.WebPageLoadTask;
import com.epam.weatherapp.util.uidecoration.ChooseLocationDecorator;
import com.epam.weatherapp.util.uidecoration.IUIDecorator;

public class LocationSelectActivity extends Activity {
    private final static String URL_ADDRESS = "http://apidev.accuweather.com/locations/v1/cities/autocomplete?apikey=hAilspiKe&language=en&q=";
    private WebPageLoadTask dataLoader;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<LocationInfo> adapter;
    private NetworkReceiver receiver;
    private ExecutorService pool;
    private IUIDecorator uiDecorator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.auto_text_location);
        pool = Executors.newFixedThreadPool(1);
        uiDecorator = new ChooseLocationDecorator();
        tuneLocationView();
        createNetworkReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(receiver);
    }

    private void tuneLocationView() {
        adapter = new ArrayAdapter<LocationInfo>(this, android.R.layout.simple_list_item_1, new ArrayList<LocationInfo>());
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.addTextChangedListener(new TextChangeWatcher());
        autoCompleteTextView.setOnItemClickListener(new LocationSelectListener());
    }

    private void createNetworkReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        this.registerReceiver(receiver, filter);
    }

    private boolean checkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = checkConnection() ? "Internet availible" : "Internet not availible";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    private class TextChangeWatcher implements TextWatcher {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String searchLocation = autoCompleteTextView.getText().toString();
            // this condition for prevent calling page loader twice when made only one change
            if (before != count && !TextUtils.isEmpty(searchLocation)) {
                cancelDataLoader();
                if (checkConnection()) {
                    addDownloadTask(searchLocation);
                }
                else {
                    inaccessibleNetMessage();
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        private void cancelDataLoader() {
            if (dataLoader != null) {
                dataLoader.cancel();
            }
        }

        private void addDownloadTask(String searchLocation) {
            String uriLocation = Uri.encode(searchLocation);
            dataLoader = new DisplayAvailableLocationTask(autoCompleteTextView, URL_ADDRESS + uriLocation,
                uiDecorator.createAfterTaskDecorator(autoCompleteTextView));
            pool.execute(dataLoader);
        }

        private void inaccessibleNetMessage() {
            Toast.makeText(LocationSelectActivity.this, "Internet not availible", Toast.LENGTH_SHORT).show();
        }
    }

    private class LocationSelectListener implements OnItemClickListener {

        @SuppressWarnings("unchecked")
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ArrayAdapter<LocationInfo> myWeatherAdapter = (ArrayAdapter<LocationInfo>) autoCompleteTextView.getAdapter();
            LocationInfo locationInfo = myWeatherAdapter.getItem(position);
            callWeatherActivity(locationInfo);
            autoCompleteTextView.setText(locationInfo.getCityName());
        }

        private void callWeatherActivity(LocationInfo locationInfo) {
            Intent intent = new Intent(LocationSelectActivity.this, LocationListActivity.class);
            //intent.putExtra(LocationWeatherFragment.LOCATION_INFO, locationInfo);
            writeLocationInfo(locationInfo);
            startActivity(intent);
        }
        
        private void writeLocationInfo(LocationInfo locationInfo) {
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(LocationListActivity.LOCATION_FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            putExtra(sharedPref, editor, locationInfo);
        }
        
        private void putExtra(SharedPreferences sharedPref, SharedPreferences.Editor editor, LocationInfo locationInfo) {
            Set<String> locationSet = getLocationSet(sharedPref);
            locationSet.add(createLocationString(locationInfo));
            editor.putStringSet(LocationListActivity.LOCATION_SET_KEY, locationSet);
            editor.commit();
        }

        private Set<String> getLocationSet(SharedPreferences sharedPref) {
            Set<String> locationSet = sharedPref.getStringSet(LocationListActivity.LOCATION_SET_KEY, null);
            if(locationSet == null) {
                locationSet = new LinkedHashSet<String>();
            }
            return locationSet;
        }
        
        private String createLocationString(LocationInfo locationInfo) {
            return locationInfo.getCityName() + "_" + locationInfo.getKey();
        }
    }
}
