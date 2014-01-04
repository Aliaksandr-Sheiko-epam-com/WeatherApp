package com.epam.weatherapp.dao;

import android.database.sqlite.SQLiteDatabase;

public abstract class SqliteAbstarctDAO implements ILocationInfoDAO {
    protected SQLiteDatabase database; 

    protected SqliteAbstarctDAO(SQLiteDatabase database) {
        this.database = database;
    }
    
    @Override
    public void close() {
        database.close();
    }
}
