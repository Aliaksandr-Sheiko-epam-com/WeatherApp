package com.epam.weatherapp.model;

public class LocationWeather {
    private final String weatherText;
    private final int weatherIcon;
    private final double metricTemperature;
    private final double imperialTemperature;
    //private final long epochTime;
    
    public LocationWeather(String weatherText, int weatherIcon, double metricTemperature, double imperialTemperature) {
        this.imperialTemperature = imperialTemperature;
        this.metricTemperature = metricTemperature;
        this.weatherIcon = weatherIcon;
        this.weatherText = weatherText;
    }

    public String getWeatherText() {
        return weatherText;
    }

    public int getWeatherIcon() {
        return weatherIcon;
    }

    public double getMetricTemperature() {
        return metricTemperature;
    }

    public double getImperialTemperature() {
        return imperialTemperature;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(imperialTemperature);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(metricTemperature);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + weatherIcon;
        result = prime * result + ((weatherText == null) ? 0 : weatherText.hashCode());
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
        LocationWeather other = (LocationWeather) obj;
        if (Double.doubleToLongBits(imperialTemperature) != Double.doubleToLongBits(other.imperialTemperature))
            return false;
        if (Double.doubleToLongBits(metricTemperature) != Double.doubleToLongBits(other.metricTemperature))
            return false;
        if (weatherIcon != other.weatherIcon)
            return false;
        if (weatherText == null) {
            if (other.weatherText != null)
                return false;
        }
        else if (!weatherText.equals(other.weatherText))
            return false;
        return true;
    }

    
}
