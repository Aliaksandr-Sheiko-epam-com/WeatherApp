package com.epam.weatherapp.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.epam.weatherapp.R;
import com.epam.weatherapp.fragment.LocationListFragment;
import com.epam.weatherapp.fragment.LocationWeatherFragment;

public class LocationWeatherActivity extends FragmentActivity implements LocationListFragment.OnHeadlineSelectedListener {
    private MenuItem screenListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_weather);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        LocationWeatherFragment locationWeather = new LocationWeatherFragment();
        locationWeather.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, locationWeather).commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && !screenListButton.isVisible()) {
            screenListButton.setVisible(true);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.location_weather_menu, menu);
        screenListButton = menu.findItem(R.id.action_add_screen);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_add_screen:
                screenListButton.setVisible(false);
                showLocationList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLocationList() {
        LocationListFragment locationListFragment = new LocationListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, locationListFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onArticleSelected(int position) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("LocationWeatherActivity---onDestroy");
    }

}
