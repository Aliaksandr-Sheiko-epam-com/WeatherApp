package com.epam.weatherapp.util.pageloader;

import android.util.Log;

import com.epam.weatherapp.exception.ReadWebPageException;

public abstract class WebPageLoadTask implements Runnable {
    private static final String EXCEPTION_TAG = "WebPageLoadTask";
    private static final int TIME_BEFORE_START_DOWNLOAD = 500;
    private volatile boolean isCancelled = false;
    private IPageDownloader pageDownloader;
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
            try {
                Thread.sleep(TIME_BEFORE_START_DOWNLOAD);
                if (!isCancelled) {
                    try {
                        String result = pageDownloader.downloadUrl(url);
                        onSuccessPostExecute(result);
                    }
                    catch (ReadWebPageException e) {
                        Log.e(EXCEPTION_TAG, "Unable to retrieve web page. URL may be invalid.", e);
                        onFailPostExecute(e);
                    }
                }
            }
            catch (InterruptedException e) {
                Log.e(EXCEPTION_TAG, "Thread-loader was interrupted", e);
            }
        }
    }

    public void cancel() {
        isCancelled = true;
    }

    protected abstract void onSuccessPostExecute(String result);

    protected abstract void onFailPostExecute(ReadWebPageException readException);
}
