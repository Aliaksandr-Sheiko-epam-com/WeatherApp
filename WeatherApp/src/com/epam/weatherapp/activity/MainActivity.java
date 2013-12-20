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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.epam.weatherapp.R;
import com.epam.weatherapp.model.LocationInfo;
import com.epam.weatherapp.util.WeatherAdapter;
import com.epam.weatherapp.util.pageloader.AvailableLocationDisplayTask;
import com.epam.weatherapp.util.pageloader.WebPageLoadTask;

public final class MainActivity extends Activity {
    public final static String COUNTRY_KEY = "com.epam.weatherapp.activity.COUNTRY_KEY";
    private final static String URL_ADDRESS = "http://apidev.accuweather.com/locations/v1/cities/autocomplete?apikey=hAilspiKe&language=en&q=";
    private WebPageLoadTask dataLoader;
    private boolean isInternetAvailible;
    private AutoCompleteTextView textView;
    private NetworkReceiver receiver;
    private ExecutorService pool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (AutoCompleteTextView) findViewById(R.id.auto_text_location);
        pool = Executors.newFixedThreadPool(1);
        checkConnection();
        tuneLocationView();
        createNetworkReceiver();
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
        textView.addTextChangedListener(new TextChangeWatcher());
        textView.setOnItemClickListener(new LocationSelectListener());
    }

    private void createNetworkReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        this.registerReceiver(receiver, filter);
    }

    private void addTextViewEmptyAdapter() { // need for start work dropdown list at once
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[] {});
        textView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private boolean checkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return isInternetAvailible = true;
        }
        else {
            return isInternetAvailible = false;
        }
    }

    private class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message;
            if (checkConnection()) {
                message = "Internet availible";
            }
            else {
                message = "Internet not availible";
            }
            showMessage(context, message);
        }

        private void showMessage(Context context, String message) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    private class TextChangeWatcher implements TextWatcher {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String searchLocation = textView.getText().toString();
            if (before != count && !TextUtils.isEmpty(searchLocation)) { // this condition for prevent calling page loader twice
                                                                         // when made only one change
                cancelDataLoader();
                if (isInternetAvailible) {
                    addDownloadTask(searchLocation);
                }
                else {
                    inaccessibleNetMessage();
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {/* NOP */
        }

        @Override
        public void afterTextChanged(Editable s) {/* NOP */
        }

        private void cancelDataLoader() {
            if (dataLoader != null) {
                dataLoader.cancel();
            }
        }

        private void addDownloadTask(String searchLocation) {
            String uriLocation = Uri.encode(searchLocation);
            dataLoader = new AvailableLocationDisplayTask(textView, MainActivity.this, URL_ADDRESS + uriLocation);
            pool.execute(dataLoader);
        }

        private void inaccessibleNetMessage() {
            Toast.makeText(MainActivity.this, "Internet not availible", Toast.LENGTH_SHORT).show();
        }
    }

    private class LocationSelectListener implements OnItemClickListener {

        @SuppressWarnings("unchecked")
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            WeatherAdapter<String> myWeatherAdapter = (WeatherAdapter<String>) textView.getAdapter();
            LocationInfo locationInfo = myWeatherAdapter.getLocationInfo(arg2);
            callWeatherActivity(locationInfo.getKey());
            textView.setText(locationInfo.getCityName());
        }

        private void callWeatherActivity(String locationKey) {
            Intent intent = new Intent(MainActivity.this, LocationWeatherActivity.class);
            intent.putExtra(COUNTRY_KEY, locationKey);
            startActivity(intent);
        }

    }
}
