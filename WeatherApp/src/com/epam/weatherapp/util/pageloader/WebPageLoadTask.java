package com.epam.weatherapp.util.pageloader;

import android.os.SystemClock;
import android.util.Log;

import com.epam.weatherapp.exception.ReadWebPageException;

public abstract class WebPageLoadTask implements Runnable {
    protected IPageDownloader pageDownloader;
    private static final String TAG_LOG = WebPageLoadTask.class.getName();
    private static final int TIME_BEFORE_START_DOWNLOAD = 500;
    private volatile boolean isCancelled = false;
    private String url;
    
    public WebPageLoadTask(String url) {
        this.url = url;
        pageDownloader = new WebPageDownloader();
    }

    public WebPageLoadTask(String url, IPageDownloader pageDownloader) {
        this.url = url;
        this.pageDownloader = pageDownloader;
    }

    @Override
    public void run() {
        if (!isCancelled) {
            sleep();
            if (!isCancelled) {
                performTask();
            }
        }
    }

    public void cancel() {
        isCancelled = true;
    }

    protected abstract void onSuccessPostExecute(String result);

    protected abstract void onFailPostExecute(ReadWebPageException readException);
    
    private void performTask() {
        try {
            String result = pageDownloader.downloadUrl(url);
            onSuccessPostExecute(result);
        }
        catch (ReadWebPageException e) {
            Log.e(TAG_LOG, "Unable to retrieve web page. URL may be invalid", e);
            onFailPostExecute(e);
        }
    }
    
    private void sleep() {
        SystemClock.sleep(TIME_BEFORE_START_DOWNLOAD);
    }
}
