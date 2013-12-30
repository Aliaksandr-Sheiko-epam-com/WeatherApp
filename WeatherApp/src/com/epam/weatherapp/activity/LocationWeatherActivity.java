package com.epam.weatherapp.activity;

import com.epam.weatherapp.R;
import com.epam.weatherapp.fragment.LocationListFragment;
import com.epam.weatherapp.fragment.LocationWeatherFragment;
import com.epam.weatherapp.model.LocationInfo;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class LocationWeatherActivity extends FragmentActivity implements LocationListFragment.OnHeadlineSelectedListener {
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

    //FIXME: use onBackPressed method here
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
        transaction.replace(R.id.pager, locationListFragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
        String[] mas = new String[] { "28580", "28800", "178087", "317676", "328328", "623", "308526"};
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = LocationWeatherFragment.create(position);
            Bundle bunle = new Bundle();
          //draft
            bunle.putSerializable(LocationWeatherFragment.LOCATION_INFO, new LocationInfo("city"+position,"","", mas[position]));

            //FIXME: setArguments method here overlaps setArguments method in create()
            fragment.setArguments(bunle);
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

    }

}
