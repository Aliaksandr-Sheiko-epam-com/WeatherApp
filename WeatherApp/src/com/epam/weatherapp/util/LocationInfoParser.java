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
        try {
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
        catch (JSONException e) {
            Log.e(TAG_LOG, "Retrieve json value error occured", e);
            throw new WeatherParseException("Retrieve json value error occured", e);
        }

    }

    private static String getKey(JSONObject locationInfo) throws JSONException {
        return locationInfo.getString(CITY_KEY);
    }

    private static String getCityName(JSONObject locationInfo) throws JSONException {
        return locationInfo.getString(LOCALIZED_NAME_KEY);
    }

    private static String getCountryName(JSONObject locationInfo) throws JSONException {
        JSONObject country = locationInfo.getJSONObject(COUNTRY_KEY);
        return country.getString(LOCALIZED_NAME_KEY);
    }

    private static String getAdministativeAreaName(JSONObject locationInfo) throws JSONException {
        JSONObject administativeArea = locationInfo.getJSONObject(ADMINISTRATIVE_AREA_KEY);
        return administativeArea.getString(LOCALIZED_NAME_KEY);
    }
}
