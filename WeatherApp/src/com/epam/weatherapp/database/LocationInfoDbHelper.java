package com.epam.weatherapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.epam.weatherapp.database.FeedReaderContract.LocationInfoEntry;

public class LocationInfoDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public final static  int DATABASE_VERSION = 1;
    public final static  String DATABASE_NAME = "Locations.db";
    
    private final static  String TEXT_TYPE = " TEXT";
    private final static  String NOT_NULL = " NOT NULL ";
    private final static  String UNIQUE = " UNIQUE ";
    private final static  String COMMA_SEP = ",";
    private final static  String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + LocationInfoEntry.TABLE_NAME + " (" +
        LocationInfoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        LocationInfoEntry.COLUMN_NAME_CITY_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEP +
        LocationInfoEntry.COLUMN_NAME_COUNTRY_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEP +
        LocationInfoEntry.COLUMN_NAME_ADMINISTRATIVE_AREA_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEP +
        LocationInfoEntry.COLUMN_NAME_KEY + TEXT_TYPE + NOT_NULL + UNIQUE +
        " )";

    private final static String SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS " + LocationInfoEntry.TABLE_NAME;

    public LocationInfoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}