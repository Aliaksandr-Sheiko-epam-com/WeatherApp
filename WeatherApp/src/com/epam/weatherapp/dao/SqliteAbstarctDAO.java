package com.epam.weatherapp.dao;

import android.database.sqlite.SQLiteDatabase;

//FIXME: do you really need this class now? Currently it is useless
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
