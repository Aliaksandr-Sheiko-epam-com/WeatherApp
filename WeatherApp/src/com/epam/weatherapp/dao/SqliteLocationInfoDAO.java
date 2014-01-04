package com.epam.weatherapp.dao;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.epam.weatherapp.database.FeedReaderContract.LocationInfoEntry;
import com.epam.weatherapp.model.LocationInfo;

public class SqliteLocationInfoDAO extends SqliteAbstarctDAO {
    private final static String EQUAL_WHERE_CONDITION = " = ?";

    public SqliteLocationInfoDAO(SQLiteDatabase database) {
        super(database);
    }

    @Override
    public List<LocationInfo> getList() {
        List<LocationInfo> LocationInfoList = new LinkedList<LocationInfo>();
        String[] projection = {LocationInfoEntry._ID, LocationInfoEntry.COLUMN_NAME_ADMINISTRATIVE_AREA_NAME,
            LocationInfoEntry.COLUMN_NAME_CITY_NAME, LocationInfoEntry.COLUMN_NAME_COUNTRY_NAME,
            LocationInfoEntry.COLUMN_NAME_KEY};
        Cursor cursor = null;
        try {
            cursor = database.query(LocationInfoEntry.TABLE_NAME, projection, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    LocationInfo locationInfo = createLocationInfo(cursor);
                    LocationInfoList.add(locationInfo);
                }
                while (cursor.moveToNext());
            }
        }
        finally {
            closeCursor(cursor);
        }
        return LocationInfoList;
    }

    @Override
    public boolean remove(LocationInfo locationInfo) {
        String selection = LocationInfoEntry._ID + EQUAL_WHERE_CONDITION;
        String[] selectionArgs = {String.valueOf(locationInfo.getId())};
        int countDeleted = database.delete(LocationInfoEntry.TABLE_NAME, selection, selectionArgs);
        return countDeleted > 0 ? true : false;
    }

    @Override
    public boolean save(LocationInfo locationInfo) {
        ContentValues values = new ContentValues();
        values.put(LocationInfoEntry.COLUMN_NAME_ADMINISTRATIVE_AREA_NAME, locationInfo.getAdministativeAreaName());
        values.put(LocationInfoEntry.COLUMN_NAME_CITY_NAME, locationInfo.getCityName());
        values.put(LocationInfoEntry.COLUMN_NAME_COUNTRY_NAME, locationInfo.getCountryName());
        values.put(LocationInfoEntry.COLUMN_NAME_KEY, locationInfo.getKey());
        long newRowId = database.insert(LocationInfoEntry.TABLE_NAME, null, values);
        return newRowId > 0 ? true : false;
    }

    @Override
    public boolean isExistKey(LocationInfo locationInfo) {
        String[] projection = {LocationInfoEntry._ID};
        String selection = LocationInfoEntry.COLUMN_NAME_KEY + EQUAL_WHERE_CONDITION;
        String[] selectionArgs = {locationInfo.getKey()};
        Cursor cursor = null;
        int changedRowCount = 0;
        try {
            cursor = database.query(LocationInfoEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
            changedRowCount = cursor.getCount();
        }
        finally {
            closeCursor(cursor);
        }
        return changedRowCount > 0 ? true : false;
    }

    @Override
    public LocationInfo fetchById(int id) {
        throw new UnsupportedOperationException();
    }

    private LocationInfo createLocationInfo(Cursor cursor) {
        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setId(cursor.getInt((cursor.getColumnIndex(LocationInfoEntry._ID))));
        locationInfo.setAdministativeAreaName(cursor.getString(cursor
            .getColumnIndex(LocationInfoEntry.COLUMN_NAME_ADMINISTRATIVE_AREA_NAME)));
        locationInfo.setCityName(cursor.getString(cursor.getColumnIndex(LocationInfoEntry.COLUMN_NAME_CITY_NAME)));
        locationInfo.setCountryName(cursor.getString(cursor.getColumnIndex(LocationInfoEntry.COLUMN_NAME_COUNTRY_NAME)));
        locationInfo.setKey(cursor.getString(cursor.getColumnIndex(LocationInfoEntry.COLUMN_NAME_KEY)));
        return locationInfo;
    }

    private void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }
}
