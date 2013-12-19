package com.epam.weatherapp.service;

import java.util.concurrent.TimeUnit;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.epam.weatherapp.exception.TechnicException;

public final class CheckWeatherService extends Service {
	private Thread checkWeatherThread;

	public void onCreate() {
		super.onCreate();
		checkWeatherThread = new Thread(new CheckWeatherThread(10));
		checkWeatherThread.start();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		checkWeatherThread.interrupt();
	}

	private class CheckWeatherThread implements Runnable {
		private int periodicity;

		public CheckWeatherThread(int periodicity) {
			this.periodicity = periodicity;
		}

		@Override
		public void run() {
			try {
				while (Thread.interrupted()) {
					sleep(periodicity);

				}
			} catch (TechnicException e) {

			}
		}

		private void sleep(int seconds) throws TechnicException {
			try {
				TimeUnit.SECONDS.sleep(periodicity);
			} catch (InterruptedException e) {
				Log.e("MyLog", "Thread was interrupted");
				throw new TechnicException(
						"Thread was sleeping when it was interrupted", e);
			}
		}
	}
}
