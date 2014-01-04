package com.epam.weatherapp.dao;

import java.util.List;

import com.epam.weatherapp.model.LocationInfo;

public interface ILocationInfoDAO {
    List<LocationInfo> getList();
    
    LocationInfo fetchById(int id);
    
    boolean remove(LocationInfo locationInfo);
    
    boolean save(LocationInfo locationInfo);
    
    boolean isExistKey(LocationInfo locationInfo);
    
    void close();
}
