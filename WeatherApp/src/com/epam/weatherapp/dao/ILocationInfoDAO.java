package com.epam.weatherapp.dao;

import java.util.List;

import com.epam.weatherapp.model.LocationInfo;

public interface ILocationInfoDAO {
    List<LocationInfo> getList();
    
    LocationInfo fetchById(int id);
    
    boolean remove(LocationInfo locationInfo);
    
    boolean save(LocationInfo locationInfo);
    
    boolean isExistKey(LocationInfo locationInfo);

    //FIXME: this method relates to the certain implementation. Remove it from the interface.
    void close();
}
