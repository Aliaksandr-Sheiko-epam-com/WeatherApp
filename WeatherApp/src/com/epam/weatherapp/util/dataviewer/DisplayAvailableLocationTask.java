package com.epam.weatherapp.util.dataviewer;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.epam.weatherapp.exception.ReadWebPageException;
import com.epam.weatherapp.exception.TechnicalException;
import com.epam.weatherapp.exception.WeatherParseException;
import com.epam.weatherapp.model.LocationInfo;
import com.epam.weatherapp.util.LocationInfoParser;
import com.epam.weatherapp.util.pageloader.IPageDownloader;
import com.epam.weatherapp.util.pageloader.WebPageLoadTask;
import com.epam.weatherapp.util.uidecoration.IResultViewer;

public final class DisplayAvailableLocationTask extends WebPageLoadTask {
    private final static String TAG_LOG = DisplayAvailableLocationTask.class.getName();
    private final IResultViewer resultViewer;
    private final View rootView;
    
    public DisplayAvailableLocationTask(View rootView, String url, IResultViewer resultViewer) {
        super(url);
        this.resultViewer = resultViewer;
        this.rootView = rootView;
    }

    public DisplayAvailableLocationTask(View rootView, String url, IPageDownloader pageDownloader, IResultViewer resultViewer) {
        super(url, pageDownloader);
        this.resultViewer = resultViewer;
        this.rootView = rootView;
    }
    
    @Override
    protected void onSuccessPostExecute(final String result) {
        Handler handler = new Handler(Looper.getMainLooper());
        try {
            final List<LocationInfo> locationList = LocationInfoParser.getLocationList(result);
            handler.post(new Runnable() {

                @Override
                public void run() {
                    try {
                        resultViewer.view(locationList);
                    }
                    catch (TechnicalException e) {
                        Log.e(TAG_LOG, "No view for dispaying data", e);
                        throw new IllegalStateException("No view for dispaying data", e);
                    }
                }
            });
        }
        catch (WeatherParseException e) {
            Log.e(TAG_LOG, "Bad data were received", e);
            handler.post(new Runnable() {
                
                @Override
                public void run() {
                    Toast.makeText(rootView.getContext(), "Bad data were received", Toast.LENGTH_SHORT).show();
                }
            }); 
        }
    }

    @Override
    protected void onFailPostExecute(ReadWebPageException readException) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                Context context = rootView.getContext();
                Toast.makeText(context, "Problem with data receiving occured", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
