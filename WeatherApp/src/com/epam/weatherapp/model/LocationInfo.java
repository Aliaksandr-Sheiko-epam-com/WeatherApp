package com.epam.weatherapp.model;

public class LocationInfo {
    private final String cityName;
    private final String countryName;
    private final String administativeAreaName;
    private final String key;

    public LocationInfo(String cityName, String countryName, String administativeAreaName, String key) {
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

    @Override
    public String toString() {
        return cityName + " : " + countryName + " : " + administativeAreaName;
    }

}