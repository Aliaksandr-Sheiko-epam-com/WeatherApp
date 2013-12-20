package com.epam.weatherapp.activity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.epam.weatherapp.R;
import com.epam.weatherapp.util.pageloader.DisplayLocationWeatherTask;

public class LocationWeatherActivity extends Activity {
    private static final String locationUrlStart = "http://apidev.accuweather.com/currentconditions/v1/";
    private static final String locationUrlEnd = ".json?language=en&apikey=hAilspiKe";
    private ExecutorService pool;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_weather);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        pool = Executors.newFixedThreadPool(1);
        tuneWebView();
        String url = locationUrlStart + getCountryKey() + locationUrlEnd;
        pool.execute(new DisplayLocationWeatherTask(LocationWeatherActivity.this, webView, url));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @SuppressLint("SetJavaScriptEnabled")
    private void tuneWebView() {
        webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    private String getCountryKey() {
        Intent intent = getIntent();
        return intent.getStringExtra(MainActivity.COUNTRY_KEY);
    }
}
