package com.epam.weatherapp.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.epam.weatherapp.exception.WeatherParseException;
import com.epam.weatherapp.model.LocationInfo;

public final class LocationInfoParser {
    private final static String EXCEPTION_TAG = "AccuweatherInfoParser";
    private final static String COUNTRY_KEY = "Country";
    private final static String ADMINISTRATIVE_AREA_KEY = "AdministrativeArea";
    private final static String LOCALIZED_NAME_KEY = "LocalizedName";
    private final static String CITY_KEY = "Key";
    private final static String MOBILE_LINK_KEY = "MobileLink";

    private LocationInfoParser() {/* NOP */
    }

    public static ArrayList<LocationInfo> getLocationList(String jsonString) throws WeatherParseException {
        JSONArray jsonArr = getJSONArray(jsonString);
        ArrayList<LocationInfo> locationList = new ArrayList<LocationInfo>();
        JSONObject locationJSONObject;
        String key, cityName, countryName, administativeAreaName;
        LocationInfo location;
        for (int i = 0; i < jsonArr.length(); i++) {
            locationJSONObject = getJSONObject(jsonArr, i);
            key = getKey(locationJSONObject);
            cityName = getCityName(locationJSONObject);
            countryName = getCountryName(locationJSONObject);
            administativeAreaName = getAdministativeAreaName(locationJSONObject);
            location = new LocationInfo(cityName, countryName, administativeAreaName, key);
            locationList.add(location);
        }
        return locationList;
    }

    public static String getMobileUrl(String jsonString) throws WeatherParseException {
        JSONArray weatherInfoArray = getJSONArray(jsonString);
        JSONObject weatherInfo = getJSONObject(weatherInfoArray, 0);
        String mobileLink = getMobileLink(weatherInfo);
        return mobileLink;
    }

    private static String getMobileLink(JSONObject weatherInfo) throws WeatherParseException {
        try {
            return weatherInfo.getString(MOBILE_LINK_KEY);
        }
        catch (JSONException e) {
            Log.e(EXCEPTION_TAG, "Retrieve MobileLink error occured", e);
            throw new WeatherParseException("Retrieve MobileLink error occured", e);
        }
    }

    private static JSONArray getJSONArray(String jsonString) throws WeatherParseException {
        try {
            return new JSONArray(jsonString);
        }
        catch (JSONException e) {
            Log.e(EXCEPTION_TAG, "Parse array result array error occured", e);
            throw new WeatherParseException("Parse array result array error occured", e);
        }
    }

    private static JSONObject getJSONObject(JSONArray jsonArr, int number) throws WeatherParseException {
        try {
            return jsonArr.getJSONObject(number);
        }
        catch (JSONException e) {
            Log.e(EXCEPTION_TAG, "Retrieve weather info object error occured", e);
            throw new WeatherParseException("Retrieve weather info object error occured", e);
        }
    }

    private static String getKey(JSONObject locationInfo) throws WeatherParseException {
        try {
            return locationInfo.getString(CITY_KEY);
        }
        catch (JSONException e) {
            Log.e(EXCEPTION_TAG, "Retrieve city key error occured", e);
            throw new WeatherParseException("Retrieve city key error occured", e);
        }
    }

    private static String getCityName(JSONObject locationInfo) throws WeatherParseException {
        try {
            return locationInfo.getString(LOCALIZED_NAME_KEY);
        }
        catch (JSONException e) {
            Log.e(EXCEPTION_TAG, "Retrieve city name error occured", e);
            throw new WeatherParseException("Retrieve city name error occured", e);
        }
    }

    private static String getCountryName(JSONObject locationInfo) throws WeatherParseException {
        try {
            JSONObject country = locationInfo.getJSONObject(COUNTRY_KEY);
            return country.getString(LOCALIZED_NAME_KEY);
        }
        catch (JSONException e) {
            Log.e(EXCEPTION_TAG, "Retrieve country name error occured", e);
            throw new WeatherParseException("Retrieve country name error occured", e);
        }
    }

    private static String getAdministativeAreaName(JSONObject locationInfo) throws WeatherParseException {
        try {
            JSONObject administativeArea = locationInfo.getJSONObject(ADMINISTRATIVE_AREA_KEY);
            return administativeArea.getString(LOCALIZED_NAME_KEY);
        }
        catch (JSONException e) {
            Log.e(EXCEPTION_TAG, "Retrieve administative area name error occured", e);
            throw new WeatherParseException("Retrieve administative area name error occured", e);
        }
    }
}
