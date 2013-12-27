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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((administativeAreaName == null) ? 0 : administativeAreaName.hashCode());
        result = prime * result + ((cityName == null) ? 0 : cityName.hashCode());
        result = prime * result + ((countryName == null) ? 0 : countryName.hashCode());
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LocationInfo other = (LocationInfo) obj;
        if (administativeAreaName == null) {
            if (other.administativeAreaName != null)
                return false;
        }
        else if (!administativeAreaName.equals(other.administativeAreaName))
            return false;
        if (cityName == null) {
            if (other.cityName != null)
                return false;
        }
        else if (!cityName.equals(other.cityName))
            return false;
        if (countryName == null) {
            if (other.countryName != null)
                return false;
        }
        else if (!countryName.equals(other.countryName))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        }
        else if (!key.equals(other.key))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return cityName + " : " + countryName + " : " + administativeAreaName;
    }

}