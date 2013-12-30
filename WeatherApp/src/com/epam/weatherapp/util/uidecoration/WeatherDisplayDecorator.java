package com.epam.weatherapp.util.uidecoration;

import android.view.View;
import android.widget.TextView;

import com.epam.weatherapp.exception.TechnicalException;
import com.epam.weatherapp.model.LocationInfo;
import com.epam.weatherapp.model.LocationWeather;
import com.epam.weatherapp.util.ViewFinder;

public class WeatherDisplayDecorator implements IUIDecorator {
    public final static String LOCATION_NAME_TAG = "location_name";
    private LocationInfo locationInfo;

    public WeatherDisplayDecorator(LocationInfo locationInfo) {
        this.locationInfo = locationInfo;
    }

    @Override
    public void showDecoration(View rootView) throws TechnicalException {
        TextView locationView = (TextView) ViewFinder.findViewByTag(LOCATION_NAME_TAG, rootView);
        locationView.setText(locationInfo.getCityName());
    }

    @Override
    public IResultViewer createAfterTaskDecorator(View rootView) {
        return new AfterTaskDecorator(rootView);
    }
    
    private class AfterTaskDecorator implements IResultViewer {
        public static final String METRIC_TEMPERATURE_TAG = "metric_temperature";
        public static final String IMPERIAL_TEMPERATURE_TAG = "imperial_temperature";
        private View rootView;

        public AfterTaskDecorator(View rootView) {
            this.rootView = rootView;
        }
        
        @Override
        public void view(Object resultData) throws TechnicalException {
            LocationWeather locationWeather = (LocationWeather) resultData;
            TextView metricTemperature = (TextView) ViewFinder.findViewByTag(METRIC_TEMPERATURE_TAG, rootView);
            TextView imperialTemperature = (TextView) ViewFinder.findViewByTag(IMPERIAL_TEMPERATURE_TAG,rootView);
            metricTemperature.setText(Double.toString(locationWeather.getMetricTemperature()));
            imperialTemperature.setText(Double.toString(locationWeather.getImperialTemperature()));
        }

    }
}
