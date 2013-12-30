package com.epam.weatherapp.util.dataviewer;

import java.util.List;

import com.epam.weatherapp.model.LocationInfo;

public interface ILocationListViewer {
    void view(List<LocationInfo> locationList);
}
