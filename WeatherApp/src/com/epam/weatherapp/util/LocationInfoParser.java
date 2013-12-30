package com.epam.weatherapp.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.epam.weatherapp.exception.WeatherParseException;
import com.epam.weatherapp.model.LocationInfo;

import android.util.Log;

public final class LocationInfoParser {
    private final static String TAG_LOG = LocationInfoParser.class.getName();
    private final static String COUNTRY_KEY = "Country";
    private final static String ADMINISTRATIVE_AREA_KEY = "AdministrativeArea";
    private final static String LOCALIZED_NAME_KEY = "LocalizedName";
    private final static String CITY_KEY = "Key";

    private LocationInfoParser() {
    }

    public static List<LocationInfo> getLocationList(String jsonString) throws WeatherParseException {
        JSONArray jsonArr = JSONParser.getJSONArray(jsonString);
        ArrayList<LocationInfo> locationList = new ArrayList<LocationInfo>();
        JSONObject locationJSONObject;
        String key, cityName, countryName, administativeAreaName;
        LocationInfo location;
        for (int i = 0; i < jsonArr.length(); i++) {
            locationJSONObject = JSONParser.getJSONObject(jsonArr, i);
            key = getKey(locationJSONObject);
            cityName = getCityName(locationJSONObject);
            countryName = getCountryName(locationJSONObject);
            administativeAreaName = getAdministativeAreaName(locationJSONObject);
            location = new LocationInfo(cityName, countryName, administativeAreaName, key);
            locationList.add(location);
        }
        return locationList;
    }

    //FIXME: there are a lot of copy pasted try\catch blocks

    private static String getKey(JSONObject locationInfo) throws WeatherParseException {
        try {
            return locationInfo.getString(CITY_KEY);
        }
        catch (JSONException e) {
            Log.e(TAG_LOG, "Retrieve city key error occured", e);
            throw new WeatherParseException("Retrieve city key error occured", e);
        }
    }

    private static String getCityName(JSONObject locationInfo) throws WeatherParseException {
        try {
            return locationInfo.getString(LOCALIZED_NAME_KEY);
        }
        catch (JSONException e) {
            Log.e(TAG_LOG, "Retrieve city name error occured", e);
            throw new WeatherParseException("Retrieve city name error occured", e);
        }
    }

    private static String getCountryName(JSONObject locationInfo) throws WeatherParseException {
        try {
            JSONObject country = locationInfo.getJSONObject(COUNTRY_KEY);
            return country.getString(LOCALIZED_NAME_KEY);
        }
        catch (JSONException e) {
            Log.e(TAG_LOG, "Retrieve country name error occured", e);
            throw new WeatherParseException("Retrieve country name error occured", e);
        }
    }

    private static String getAdministativeAreaName(JSONObject locationInfo) throws WeatherParseException {
        try {
            JSONObject administativeArea = locationInfo.getJSONObject(ADMINISTRATIVE_AREA_KEY);
            return administativeArea.getString(LOCALIZED_NAME_KEY);
        }
        catch (JSONException e) {
            Log.e(TAG_LOG, "Retrieve administative area name error occured", e);
            throw new WeatherParseException("Retrieve administative area name error occured", e);
        }
    }
}
