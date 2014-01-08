package com.epam.weatherapp.activity;

import java.util.LinkedList;
import java.util.List;

import com.epam.weatherapp.R;
import com.epam.weatherapp.dao.ILocationInfoDAO;
import com.epam.weatherapp.dao.SqliteLocationInfoDAO;
import com.epam.weatherapp.database.LocationInfoDbHelper;
import com.epam.weatherapp.model.LocationInfo;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LocationListActivity extends Activity {
    private ILocationInfoDAO locationInfoDAO;
    private ViewGroup containerView;
    private List<LocationInfo> locationInfoList;
    private MenuItem weatherMenuItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_location_list, menu);
        weatherMenuItem = menu.findItem(R.id.action_show_weather_item);
        setEmptyActivityState();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_location:
                callAddLocationActivity();
                return true;
            case R.id.action_show_weather_item:
                callWeathewListActivity(); 
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);
        initLocationInfoDAO();

        //FIXME: this definitely must be ListView
        containerView = (ViewGroup) findViewById(R.id.container);   
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationInfoList = locationInfoDAO.getList();
        deleteAllItems();
        createItems();
        if(weatherMenuItem != null) {
            setEmptyActivityState();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //FIXME: Imagine the following case:
        // start LocationListActivity (open DB), start LocationSelectActivity (open DB), press back -- destroy LocationSelectActivity (close DB).
        // We are in LocationListActivity but the db was closed in LocationSelectActivity.
        locationInfoDAO.close();
    }
    
    private void deleteAllItems() {
        containerView.removeAllViewsInLayout();
    }
    
    private void setEmptyActivityState() {
        boolean epmty = locationInfoList.isEmpty(); 
        weatherMenuItem.setEnabled(!epmty);
        if(epmty) {
            //FIXME: use setEmptyView method of ListView
            findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
        } 
    }
    
    private void callWeathewListActivity() {
        Intent intent = new Intent(LocationListActivity.this, WeatherListActivity.class);
        intent.putExtra(WeatherListActivity.LOCATION_INFO_LIST, (LinkedList<LocationInfo>) locationInfoList);
        startActivity(intent);
    }
    
    private void callAddLocationActivity() {
        Intent intent = new Intent(LocationListActivity.this, LocationSelectActivity.class);
        //FIXME: should use startActivityForResult here!! Currently you can have multiple LocationListActivity in one history stack
        // Take a look here: http://developer.android.com/guide/components/tasks-and-back-stack.html
        startActivity(intent);
    }

    private void createItems() {
        for (int i = 0; i < locationInfoList.size(); i++) {
            addItem(locationInfoList.get(i));
        }
        hideEmptyMessage();
    }

    private void hideEmptyMessage() {
        if (containerView.getChildCount() != 0) {
            //FIXME: use setEmptyView method of ListView
            findViewById(android.R.id.empty).setVisibility(View.GONE);
        }
    }

    private void initLocationInfoDAO() {
        //FIXME: copypasted from LocationSelectActivity. Think how to combine
        LocationInfoDbHelper locationInfoDbHelper = new LocationInfoDbHelper(getApplicationContext());
        SQLiteDatabase writeDb = locationInfoDbHelper.getWritableDatabase();
        locationInfoDAO = new SqliteLocationInfoDAO(writeDb);
    }
    
    private void addItem(final LocationInfo locationInfo) {
        final ViewGroup newView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.location_item_list, containerView,
            false);
        ((TextView) newView.findViewById(android.R.id.text1)).setText(locationInfo.getCityName());
        newView.findViewById(R.id.delete_button).setOnClickListener(new RemoveItemListener(newView, locationInfo));
        containerView.addView(newView, 0);
    }

    private class RemoveItemListener implements View.OnClickListener {
        private View newView;
        private LocationInfo locationInfo;
        
        public RemoveItemListener(View newView, LocationInfo locationInfo) {
            this.locationInfo = locationInfo;
            this.newView = newView;
        }
        
        @Override
        public void onClick(View v) {
            containerView.removeView(newView);
            removeFromDataBase(locationInfo);
            locationInfoList.remove(locationInfo);
            setEmptyActivityState();
        }

        private void removeFromDataBase(LocationInfo locationInfo) {
            locationInfoDAO.remove(locationInfo);
        }
    }
}
