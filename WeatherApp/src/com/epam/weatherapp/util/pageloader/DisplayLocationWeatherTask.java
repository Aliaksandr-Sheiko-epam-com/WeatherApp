package com.epam.weatherapp.util.pageloader;

import android.app.Activity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.epam.weatherapp.exception.ReadWebPageException;
import com.epam.weatherapp.exception.WeatherParseException;
import com.epam.weatherapp.util.LocationInfoParser;

public final class DisplayLocationWeatherTask extends WebPageLoadTask {
    private final static String EXCEPTION_TAG = "DisplayLocationWeatherTask";
    private Activity activity;
    private WebView webView;

	public DisplayLocationWeatherTask(Activity activity, WebView webView, String url) {
		super(url);
		this.activity = activity;
		this.webView = webView;
	}

	@Override
	protected void onSuccessPostExecute(String result) {
	    try {
            final String mobileUrl = LocationInfoParser.getMobileUrl(result);
            activity.runOnUiThread(new Runnable() {
                
                @Override
                public void run() {
                    webView.loadUrl(mobileUrl);
                }
            });
        }
        catch (WeatherParseException e) {
            Toast.makeText(activity, "Bad data were received", Toast.LENGTH_SHORT).show();
            Log.e(EXCEPTION_TAG, "Bad data were received", e);
        }
	}

	@Override
	protected void onFailPostExecute(ReadWebPageException readException) {
	    activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(activity, "Problem with data receiving occured", Toast.LENGTH_SHORT).show();
            }
        });
	}

}
