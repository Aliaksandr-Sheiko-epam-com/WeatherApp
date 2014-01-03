package com.epam.weatherapp.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.epam.weatherapp.R;
import com.epam.weatherapp.fragment.LocationListFragment;
import com.epam.weatherapp.fragment.LocationWeatherFragment;
import com.epam.weatherapp.model.LocationInfo;

public class WeatherListActivity extends FragmentActivity implements LocationListFragment.OnHeadlineSelectedListener {
    private static final int NUM_PAGES = 5;
    private MenuItem screenListButton;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slider);
        createBackButtonMenu();
        setUpAdapter();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!screenListButton.isVisible()) {
            screenListButton.setVisible(true);
        }
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
        Intent intent = new Intent(WeatherListActivity.this, LocationListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onArticleSelected(int position) {
    }
    
    private void setUpAdapter() {
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) { 
                invalidateOptionsMenu();
            }
        });
    }
    
    private void createBackButtonMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter  {
        //draft
        String[] mas = new String[] { "28580", "28801", "178087", "317676", "328328", "623", "308526"};
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = LocationWeatherFragment.create(position);
            Bundle bundle = fragment.getArguments();
            bundle.putSerializable(LocationWeatherFragment.LOCATION_INFO, new LocationInfo("city"+position,"","", mas[position]));
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

    }

}
