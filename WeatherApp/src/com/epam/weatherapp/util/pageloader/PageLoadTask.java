package com.epam.weatherapp.util.pageloader;

import android.util.Log;

import com.epam.weatherapp.exception.TechnicException;
import com.epam.weatherapp.util.Constant;

public class PageLoadTask implements Runnable{
	private static final int TIME_BEFORE_START_DOWNLOAD = 500;
	private volatile boolean isCancelled = false;
	private IPageDownloader pageDownloader;
	private String url;
	
	public PageLoadTask(String url) {
		this.url = url;
		pageDownloader = new WebPageDownloader();
	}
	
	public PageLoadTask(String url, IPageDownloader pageDownloader) {
		this.url = url;
		this.pageDownloader = pageDownloader;
	}
	
	@Override
	public void run() {
		if (!isCancelled) {
			try {
				Thread.sleep(TIME_BEFORE_START_DOWNLOAD);
				if (!isCancelled) {
					String result = pageDownloader.downloadUrl(url);
					onPostExecute(result);
				}
			} catch (InterruptedException e) {
				Log.e(Constant.EXCEPTION_TAG,
						"Thread-loader data was interrupted");
			} catch (TechnicException e) {
				Log.e(Constant.EXCEPTION_TAG,
						"Unable to retrieve web page. URL may be invalid.");
			}
		}
	}
	
	public void cancel() {
		isCancelled = true;
	}

	protected void onPostExecute(String result) {/*NOP*/}
}
