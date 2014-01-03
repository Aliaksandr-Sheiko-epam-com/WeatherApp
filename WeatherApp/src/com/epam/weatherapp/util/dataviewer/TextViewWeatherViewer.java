package com.epam.weatherapp.util.dataviewer;

import android.view.View;
import android.widget.TextView;

import com.epam.weatherapp.exception.TechnicalException;
import com.epam.weatherapp.model.LocationWeather;
import com.epam.weatherapp.util.ViewFinder;

public final class TextViewWeatherViewer implements ILocationWeatherViewer {
    public static final String METRIC_TEMPERATURE_TAG = "metric_temperature";
    public static final String IMPERIAL_TEMPERATURE_TAG = "imperial_temperature";
    private final View rootView;

    public TextViewWeatherViewer(View rootView) {
        this.rootView = rootView;
    }

    @Override
    public void view(LocationWeather locationWeather) throws TechnicalException {
        ViewFinder<TextView> viewFinder = new ViewFinder<TextView>(rootView);
        TextView metricTemperature = viewFinder.findViewByTag(METRIC_TEMPERATURE_TAG);
        TextView imperialTemperature = viewFinder.findViewByTag(IMPERIAL_TEMPERATURE_TAG);
        metricTemperature.setText(Double.toString(locationWeather.getMetricTemperature()));
        imperialTemperature.setText(Double.toString(locationWeather.getImperialTemperature()));
    }
}
