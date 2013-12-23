package com.epam.weatherapp.util.pageloader;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.epam.weatherapp.exception.ReadWebPageException;
import com.epam.weatherapp.exception.WeatherParseException;
import com.epam.weatherapp.model.LocationInfo;
import com.epam.weatherapp.util.LocationInfoParser;

public final class AvailableLocationDisplayTask extends WebPageLoadTask {
    private final static String TAG_LOG = AvailableLocationDisplayTask.class.getName();
    private AutoCompleteTextView autoCompleteTextView;
    private Context context;

    public AvailableLocationDisplayTask(AutoCompleteTextView textView, Context context, String url) {
        super(url);
        this.autoCompleteTextView = textView;
        this.context = context;
    }

    public AvailableLocationDisplayTask(AutoCompleteTextView textView, Context context, String url, IPageDownloader pageDownloader) {
        super(url, pageDownloader);
        this.autoCompleteTextView = textView;
        this.context = context;
    }

    @Override
    protected void onSuccessPostExecute(final String result) {
        try {
            final ArrayList<LocationInfo> locationList = LocationInfoParser.getLocationList(result);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    ArrayAdapter<LocationInfo> adapter = new ArrayAdapter<LocationInfo>(context,
                        android.R.layout.simple_list_item_1, locationList);
                    autoCompleteTextView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            });
        }
        catch (WeatherParseException e) {
            Toast.makeText(context, "Bad data were received", Toast.LENGTH_SHORT).show();
            Log.e(TAG_LOG, "Bad data were received", e);
        }
    }

    @Override
    protected void onFailPostExecute(ReadWebPageException readException) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(context, "Problem with data receiving occured", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
