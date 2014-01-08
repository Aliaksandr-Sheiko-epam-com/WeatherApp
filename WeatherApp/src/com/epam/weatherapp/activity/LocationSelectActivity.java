package com.epam.weatherapp.activity;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.epam.weatherapp.R;
import com.epam.weatherapp.dao.ILocationInfoDAO;
import com.epam.weatherapp.dao.SqliteLocationInfoDAO;
import com.epam.weatherapp.database.LocationInfoDbHelper;
import com.epam.weatherapp.model.LocationInfo;
import com.epam.weatherapp.util.dataviewer.DisplayAvailableLocationTask;
import com.epam.weatherapp.util.pageloader.WebPageLoadTask;
import com.epam.weatherapp.util.uidecoration.ChooseLocationDecorator;
import com.epam.weatherapp.util.uidecoration.IUIDecorator;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
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

public class LocationSelectActivity extends Activity {
    private final static String URL_ADDRESS = "http://apidev.accuweather.com/locations/v1/cities/autocomplete?apikey=hAilspiKe&language=en&q=";
    private WebPageLoadTask dataLoader;
    private NetworkReceiver receiver;
    private ExecutorService pool;
    private ILocationInfoDAO locationInfoDAO;
    private IUIDecorator uiDecorator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_select);
        pool = Executors.newFixedThreadPool(1);
        uiDecorator = new ChooseLocationDecorator();
        initLocationInfoDAO();
        tuneLocationView();
        createNetworkReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(receiver);
        locationInfoDAO.close();

        // FIXME: in this case the method does nothing. Whether remove it or use another one.
        pool.shutdown();
    }
    
    private void initLocationInfoDAO() {
        LocationInfoDbHelper locationInfoDbHelper = new LocationInfoDbHelper(getApplicationContext());
        SQLiteDatabase writeDb = locationInfoDbHelper.getWritableDatabase();
        locationInfoDAO = new SqliteLocationInfoDAO(writeDb);
    }

    private void tuneLocationView() {
        ArrayAdapter<LocationInfo> adapter = new ArrayAdapter<LocationInfo>(this, android.R.layout.simple_list_item_1, new ArrayList<LocationInfo>());
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.auto_text_location);
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
            showToastMessage(message);
        }
    }
    
    private void showToastMessage(String message) {
        Toast.makeText(LocationSelectActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private class TextChangeWatcher implements TextWatcher {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.auto_text_location);
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
            AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.auto_text_location);
            dataLoader = new DisplayAvailableLocationTask(autoCompleteTextView, URL_ADDRESS + uriLocation,
                uiDecorator.createAfterTaskDecorator(autoCompleteTextView));
            pool.execute(dataLoader);
        }

        private void inaccessibleNetMessage() {
            showToastMessage("Internet not availible");
        }
    }

    private class LocationSelectListener implements OnItemClickListener {

        @SuppressWarnings("unchecked")
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.auto_text_location);
            ArrayAdapter<LocationInfo> myWeatherAdapter = (ArrayAdapter<LocationInfo>) autoCompleteTextView.getAdapter();
            LocationInfo locationInfo = myWeatherAdapter.getItem(position);
            checkIfExistAndCallNextActivity(locationInfo);
            autoCompleteTextView.setText(locationInfo.getCityName());
        }

        private void checkIfExistAndCallNextActivity(LocationInfo locationInfo) {
            if(!locationInfoDAO.isExistKey(locationInfo)) {
                locationInfoDAO.save(locationInfo);
                callWeatherActivity();
            } else {
                showToastMessage("This city is already added");
            }
        }
        
        private void callWeatherActivity() {
            Intent intent = new Intent(LocationSelectActivity.this, LocationListActivity.class);           
            startActivity(intent);
        }
    }
}
