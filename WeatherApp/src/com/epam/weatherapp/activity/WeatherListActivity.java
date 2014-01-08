package com.epam.weatherapp.activity;

import java.util.List;

import com.epam.weatherapp.R;
import com.epam.weatherapp.fragment.LocationWeatherFragment;
import com.epam.weatherapp.model.LocationInfo;

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
import android.view.MenuItem;

public class WeatherListActivity extends FragmentActivity {
    public final static String LOCATION_INFO_LIST = "location_info_list";
    private List<LocationInfo> locationInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_list);
        createBackButtonMenu();
        initLocationInfoList();
        setUpPageAdapter();
    }

    @SuppressWarnings("unchecked")
    private void initLocationInfoList() {
        Intent intent = getIntent();
        locationInfoList = (List<LocationInfo>) intent.getSerializableExtra(LOCATION_INFO_LIST);
        checkLocationInfoList();
    }

    private void checkLocationInfoList() {
        if (locationInfoList == null) {
            throw new IllegalStateException("LocationInfoList is empty received");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpPageAdapter() {
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
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

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //FIXME: why you don't want to pass location info to the create method
            Fragment fragment = LocationWeatherFragment.create(position);
            Bundle bundle = fragment.getArguments();
            bundle.putSerializable(LocationWeatherFragment.LOCATION_INFO, locationInfoList.get(position));
            return fragment;
        }

        @Override
        public int getCount() {
            return locationInfoList.size();
        }
    }

}
