package com.epam.weatherapp.database;

import android.provider.BaseColumns;

public class FeedReaderContract {
    public FeedReaderContract() {
    }

    /* Inner class that defines the table contents */
    public static interface LocationInfoEntry extends BaseColumns {
        public final static String TABLE_NAME = "location_info";
        public final static String COLUMN_NAME_CITY_NAME = "city_name";
        public final static String COLUMN_NAME_COUNTRY_NAME = "country_name";
        public final static String COLUMN_NAME_KEY = "key";
        public final static String COLUMN_NAME_ADMINISTRATIVE_AREA_NAME = "administrative_area_name";
    }
}
