package com.epam.weatherapp.util.dataviewer;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.epam.weatherapp.exception.ReadWebPageException;
import com.epam.weatherapp.exception.TechnicalException;
import com.epam.weatherapp.exception.WeatherParseException;
import com.epam.weatherapp.model.LocationWeather;
import com.epam.weatherapp.util.LocationWeatherParser;
import com.epam.weatherapp.util.pageloader.IPageDownloader;
import com.epam.weatherapp.util.pageloader.WebPageLoadTask;
import com.epam.weatherapp.util.uidecoration.IResultViewer;

public final class DisplayLocationWeatherTask extends WebPageLoadTask {
    private final static String TAG_LOG = DisplayLocationWeatherTask.class.getName();
    private final View rootView;
    private final IResultViewer resultViewer;
    
    public DisplayLocationWeatherTask(View rootView, String url, IResultViewer resultViewer) {
        super(url);
        this.resultViewer = resultViewer;
        this.rootView = rootView;
    }

    public DisplayLocationWeatherTask(View rootView, String url, IPageDownloader pageDownloader, IResultViewer resultViewer) {
        super(url, pageDownloader);
        this.resultViewer = resultViewer;
        this.rootView = rootView;
    }

	@Override
	protected void onSuccessPostExecute(String result) {
	    Handler handler = new Handler(Looper.getMainLooper());
	    try {
	        final LocationWeather locationWeather = LocationWeatherParser.getLocationWeather(result);
            handler.post(new Runnable() {
                
                @Override
                public void run() {
                    try {
                        resultViewer.view(locationWeather);
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
                Toast.makeText(rootView.getContext(), "Problem with data receiving occured", Toast.LENGTH_SHORT).show();
            }
        });
	}
}
