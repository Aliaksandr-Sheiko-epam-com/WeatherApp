package com.epam.weatherapp.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.ParseException;
import android.util.Log;

import com.epam.weatherapp.exception.TechnicException;

public final class AccuweatherInfoParser {
	private final static String COUNTRY_KEY = "Country";
	private final static String ADMINISTRATIVE_AREA_KEY = "AdministrativeArea";
	private final static String LOCALIZED_NAME_KEY = "LocalizedName";
	private final static String CITY_KEY = "Key";

	private AccuweatherInfoParser() {/* NOP */
	}

	public static ArrayList<LocationInfo> getLocationList(String jsonString)
			throws TechnicException {
		try {
			JSONArray jsonArr = new JSONArray(jsonString);
			ArrayList<LocationInfo> locationList = new ArrayList<LocationInfo>();
			JSONObject locationInfo;
			String key, cityName, countryName, administativeAreaName;
			LocationInfo location;
			for (int i = 0; i < jsonArr.length(); i++) {
				locationInfo = getLocationInfo(jsonArr, i);
				key = getKey(locationInfo);
				cityName = getCityName(locationInfo);
				countryName = getCountryName(locationInfo);
				administativeAreaName = getAdministativeAreaName(locationInfo);
				location = new LocationInfo(cityName, countryName, administativeAreaName, key);
				locationList.add(location);
			}
			return locationList;
		} catch (ParseException e) {
			Log.e(Constant.EXCEPTION_TAG, "");
			throw new TechnicException("Parse error occured", e);
		} catch (JSONException e) {
			Log.e(Constant.EXCEPTION_TAG, e.toString());
			throw new TechnicException("Parse error occured with string: " + jsonString , e);
		}
	}

	private static JSONObject getLocationInfo(JSONArray jsonArr, int number) throws TechnicException {
		try {
			return jsonArr.getJSONObject(number);
		} catch (JSONException e) {
			Log.e(Constant.EXCEPTION_TAG, e.toString());
			throw new TechnicException("Parse error occured", e);
		}
	}
	
	private static String getKey(JSONObject locationInfo)
			throws TechnicException {
		try {
			return locationInfo.getString(CITY_KEY);
		} catch (JSONException e) {
			Log.e(Constant.EXCEPTION_TAG, e.toString());
			throw new TechnicException("Parse error occured", e);
		}
	}
	
	private static String getCityName(JSONObject locationInfo)
			throws TechnicException {
		try {
			return locationInfo.getString(LOCALIZED_NAME_KEY);
		} catch (JSONException e) {
			Log.e(Constant.EXCEPTION_TAG, e.toString());
			throw new TechnicException("Parse error occured", e);
		}
	}

	private static String getCountryName(JSONObject locationInfo)
			throws TechnicException {
		try {
			JSONObject country = locationInfo.getJSONObject(COUNTRY_KEY);
			return country.getString(LOCALIZED_NAME_KEY);
		} catch (JSONException e) {
			Log.e(Constant.EXCEPTION_TAG, e.toString());
			throw new TechnicException("Parse error occured", e);
		}
	}

	private static String getAdministativeAreaName(JSONObject locationInfo) throws TechnicException {
		try {
			JSONObject administativeArea = locationInfo
					.getJSONObject(ADMINISTRATIVE_AREA_KEY);
			return administativeArea.getString(LOCALIZED_NAME_KEY);
		} catch (JSONException e) {
			Log.e(Constant.EXCEPTION_TAG, e.toString());
			throw new TechnicException("Parse error occured", e);
		}
	}

	public static class LocationInfo {
		private String cityName;
		private String countryName;
		private String administativeAreaName;
		private String key;

		private LocationInfo(String cityName, String countryName,
				String administativeAreaName, String key) {
			this.cityName = cityName;
			this.countryName = countryName;
			this.administativeAreaName = administativeAreaName;
			this.key = key;
		}

		public String getCityName() {
			return cityName;
		}

		public String getCountryName() {
			return countryName;
		}

		public String getAdministativeAreaName() {
			return administativeAreaName;
		}

		public String getKey() {
			return key;
		}
	}

}
