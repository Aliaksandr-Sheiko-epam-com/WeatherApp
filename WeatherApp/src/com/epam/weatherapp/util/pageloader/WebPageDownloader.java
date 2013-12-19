package com.epam.weatherapp.util.pageloader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

import com.epam.weatherapp.exception.TechnicException;
import com.epam.weatherapp.util.Constant;

public class WebPageDownloader implements IPageDownloader{
	private static final String REQUEST_METHOD = "GET";
	private static final String CHARACTER_ENCODING = "UTF-8";
	private static final int BUFFER_SIZE = 500;
	private static final int CONNECT_TIMEOUT = 15000;
	private static final int READ_TIMEOUT = 10000;

	@Override
	public String downloadUrl(String url) throws TechnicException {
		HttpURLConnection connection = createConnection(url);
		String contentAsString = readURLResponse(connection);
		return contentAsString;
	}

	private HttpURLConnection createConnection(String stringURL)
			throws TechnicException {
		try {
			URL url = new URL(stringURL);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setReadTimeout(READ_TIMEOUT);
			connection.setConnectTimeout(CONNECT_TIMEOUT);
			connection.setRequestMethod(REQUEST_METHOD);
			connection.setDoInput(true);
			connection.connect();
			return connection;
		} catch (IOException e) {
			Log.e(Constant.EXCEPTION_TAG,
					"Some problem occured when connection was tried to establish.");
			throw new TechnicException("Try to establish connection", e);
		}
	}

	private String readURLResponse(HttpURLConnection connection)
			throws TechnicException {
		Reader reader = null;
		TechnicException exception = null;
		try {
			reader = new InputStreamReader(connection.getInputStream(),
					CHARACTER_ENCODING);
			return readtreamAsString(reader);
		} catch (UnsupportedEncodingException e) {
			Log.e(Constant.EXCEPTION_TAG,
					"Unsupported encoding was in resonse.");
			exception = new TechnicException(
					"Unsupported encoding was in resonse", e);
			throw exception;
		} catch (IOException e) {
			Log.e(Constant.EXCEPTION_TAG,
					"Input exception occured when response was been reading.");
			exception = new TechnicException(
					"Input exception occured when response was being read.", e);
			throw exception;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					Log.e(Constant.EXCEPTION_TAG,
							"Input exception occured when trying to stream was closed");
					if (exception != null) {
						e.initCause(exception);
					}
					throw new TechnicException(
							"Input exception occured when trying to stream was closed",
							e);
				}
			}
		}
	}

	private String readtreamAsString(Reader reader) throws IOException {
		char[] buffer = new char[BUFFER_SIZE];
		StringBuilder content = new StringBuilder();
		int count;
		while ((count = reader.read(buffer)) != -1) {
			content.append(buffer, 0, count);
		}
		return content.toString();
	}

}
