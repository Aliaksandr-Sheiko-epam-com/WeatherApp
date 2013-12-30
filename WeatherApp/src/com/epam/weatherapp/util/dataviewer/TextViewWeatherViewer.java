package com.epam.weatherapp.util.dataviewer;

import android.view.View;
import android.widget.TextView;

import com.epam.weatherapp.model.LocationWeather;

public final class TextViewWeatherViewer implements ILocationWeatherViewer {
    public static final String METRIC_TEMPERATURE_TAG = "metric_temperature";
    public static final String IMPERIAL_TEMPERATURE_TAG = "imperial_temperature";
    private final View rootView;

    public TextViewWeatherViewer(View rootView) {
        this.rootView = rootView;
    }

    @Override
    public void view(LocationWeather locationWeather) {
        TextView metricTemperature = (TextView) findViewByTag(METRIC_TEMPERATURE_TAG);
        TextView imperialTemperature = (TextView) findViewByTag(IMPERIAL_TEMPERATURE_TAG);
        metricTemperature.setText(Double.toString(locationWeather.getMetricTemperature()));
        imperialTemperature.setText(Double.toString(locationWeather.getImperialTemperature()));
    }

    private View findViewByTag(String tagName) {
        return rootView.findViewWithTag(tagName);
    }
}
