package com.epam.weatherapp.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.epam.weatherapp.exception.WeatherParseException;

public final class JSONParser {
    private final static String TAG_LOG = JSONParser.class.getName();
    
    private JSONParser() {
    }

    public static JSONArray getJSONArray(String jsonString) throws WeatherParseException {
        try {
            return new JSONArray(jsonString);
        }
        catch (JSONException e) {
            Log.e(TAG_LOG, "Parse array result array error occured", e);
            throw new WeatherParseException("Parse array result array error occured", e);
        }
    }

    public static JSONObject getJSONObject(JSONArray jsonArr, int number) throws WeatherParseException {
        try {
            return jsonArr.getJSONObject(number);
        }
        catch (JSONException e) {
            Log.e(TAG_LOG, "Retrieve weather info object error occured", e);
            throw new WeatherParseException("Retrieve weather info object error occured", e);
        }
    }
}
