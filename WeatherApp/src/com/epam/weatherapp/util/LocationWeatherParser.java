package com.epam.weatherapp.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.epam.weatherapp.exception.WeatherParseException;
import com.epam.weatherapp.model.LocationWeather;

import android.util.Log;

public final class LocationWeatherParser {
    private final static String TAG_LOG = LocationWeatherParser.class.getName();
    private final static String WEATHER_ICON_KEY = "WeatherIcon";
    private final static String METRIC_KEY = "Metric";
    private final static String TEMPERATURE_KEY = "Temperature";
    private final static String VALUE_KEY = "Value";
    private final static String WEATHER_TEXT_KEY = "WeatherText";
    private final static String IMPERIAL_KEY = "Imperial";
    
    private LocationWeatherParser() {}

    public static LocationWeather getLocationWeather(String jsonString) throws WeatherParseException {
        JSONArray jsonArr = JSONParser.getJSONArray(jsonString);
        JSONObject weatherJSONObject = JSONParser.getJSONObject(jsonArr, 0);
        int weatherIcon = getWeatherIcon(weatherJSONObject);
        String weatherText = getWeatherText(weatherJSONObject);
        double metricTemperature = getMetricTemperature(weatherJSONObject);
        double imperialTemperature = getImperialTemperature(weatherJSONObject);
        LocationWeather locationWeather = new LocationWeather(weatherText, weatherIcon, metricTemperature, imperialTemperature);
        return locationWeather;
    }

    //FIXME: there are a lot of copy pasted try\catch blocks
    
    private static int getWeatherIcon(JSONObject weatherJSONObject) throws WeatherParseException {
        try {
            return  weatherJSONObject.getInt(WEATHER_ICON_KEY);
        }
        catch (JSONException e) {
            Log.e(TAG_LOG, "Retrieve weather icon error occured", e);
            throw new WeatherParseException("Retrieve weather icon error occured", e);
        }
    }
    
    private static double getMetricTemperature(JSONObject weatherJSONObject) throws WeatherParseException {
        try {
            JSONObject temperatureObject = weatherJSONObject.getJSONObject(TEMPERATURE_KEY);
            JSONObject temperatureMetric = temperatureObject.getJSONObject(METRIC_KEY);
            return temperatureMetric.getDouble(VALUE_KEY);
        }
        catch (JSONException e) {
            Log.e(TAG_LOG, "Retrieve metric temperature error occured", e);
            throw new WeatherParseException("Retrieve metric temperature error occured", e);
        }
    }
    
    private static double getImperialTemperature(JSONObject weatherJSONObject) throws WeatherParseException {
        try {
            JSONObject temperatureObject = weatherJSONObject.getJSONObject(TEMPERATURE_KEY);
            JSONObject temperatureMetric = temperatureObject.getJSONObject(IMPERIAL_KEY);
            return temperatureMetric.getDouble(VALUE_KEY);
        }
        catch (JSONException e) {
            Log.e(TAG_LOG, "Retrieve metric temperature error occured", e);
            throw new WeatherParseException("Retrieve metric temperature error occured", e);
        }
    }
    
    private static String getWeatherText(JSONObject weatherJSONObject) throws WeatherParseException {
        try {
            return weatherJSONObject.getString(WEATHER_TEXT_KEY);
        }
        catch (JSONException e) {
            Log.e(TAG_LOG, "Retrieve weather text error occured", e);
            throw new WeatherParseException("Retrieve weather text error occured", e);
        }
    }
}
