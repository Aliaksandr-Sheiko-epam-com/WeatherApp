package com.epam.weatherapp.exception;

public class WeatherParseException extends Exception {

	private static final long serialVersionUID = 958974587777144017L;

	public WeatherParseException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
	}

	public WeatherParseException(String detailMessage) {
		super(detailMessage);
	}

	public WeatherParseException(Throwable cause) {
		super(cause);
	}
	
	
}
