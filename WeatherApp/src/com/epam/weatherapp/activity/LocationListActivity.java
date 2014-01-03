package com.epam.weatherapp.activity;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epam.weatherapp.R;

public class LocationListActivity extends Activity {
    public final static String LOCATION_FILE_NAME = "com.epam.weatherapp.activity.LOCATIONS_FILE_KEY";
    public final static String LOCATION_SET_KEY = "locations";
    
    private ViewGroup mContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);
        mContainerView = (ViewGroup) findViewById(R.id.container);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_location_list, menu);
        return true;
    }
    
    @Override
    public void onStart() {
        super.onStart();
        Set<String> locationSet = getLocationSet();
        createItems(locationSet);
        hideEmptyMessage();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case android.R.id.home:
                // Navigate "up" the demo structure to the launchpad activity.
                // See http://developer.android.com/design/patterns/navigation.html for more.
                NavUtils.navigateUpTo(this, new Intent(this, LocationSelectActivity.class));
                return true;*/

            case R.id.action_add_location:
                // Hide the "empty" view since there is now at least one item in the list.
                //findViewById(android.R.id.empty).setVisibility(View.GONE);
                addLocation();
                return true;
            case R.id.action_show_weather_item:
                
                return true;
            default: 
                return super.onOptionsItemSelected(item);
        }
    }

    private void addLocation() {
        Intent intent = new Intent(LocationListActivity.this, LocationSelectActivity.class);
        startActivity(intent);
    }

    private void addItem(String viewInfo) {
        // Instantiate a new "row" view.
        final ViewGroup newView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.location_item_list, mContainerView, false);

        String[] locationInfoPart = viewInfo.split("_");
        // Set the text in the new row to a random country.
        ((TextView) newView.findViewById(android.R.id.text1)).setText(
            locationInfoPart[0]);
        
        // Set a click listener for the "X" button in the row that will remove the row.
        newView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove the row from its parent (the container view).
                // Because mContainerView has android:animateLayoutChanges set to true,
                // this removal is automatically animated.
                mContainerView.removeView(newView);
                String cityName = (String) ((TextView) view).getText();
                removeFromPreferences(cityName);

                // If there are no rows remaining, show the empty view.
                if (mContainerView.getChildCount() == 0) {
                    findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
                }
            }

            private void removeFromPreferences(String removeCityName) {
                
                Set<String> locationSet = getLocationSet();
                Set<String> newLocationSet = new LinkedHashSet<String>();
                Iterator<String> it = locationSet.iterator();
                while(it.hasNext()) {
                    String cityName = it.next();
                    //if(cityName) {}
                }
                
            }
        });

        // Because mContainerView has android:animateLayoutChanges set to true,
        // adding this view is automatically animated.
        mContainerView.addView(newView, 0);
    }

    private void hideEmptyMessage() {
        if(mContainerView.getChildCount() != 0) {
            findViewById(android.R.id.empty).setVisibility(View.GONE);
        }
    }
    
    private void createItems(Set<String> locationSet) {
        if(locationSet == null) {
            return;
        }
        Iterator<String> it = locationSet.iterator();
        while(it.hasNext()) {
            addItem(it.next());
        }
    }

    private Set<String> getLocationSet() {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(LOCATION_FILE_NAME, Context.MODE_PRIVATE);
        Set<String> locationSet = sharedPref.getStringSet(LOCATION_SET_KEY, null);
        return locationSet;
    }

}
