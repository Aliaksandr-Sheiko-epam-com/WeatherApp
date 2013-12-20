package com.epam.weatherapp.exception;

public class ReadWebPageException extends Exception{
	
	private static final long serialVersionUID = -8953128946600381680L;

	public ReadWebPageException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
	}

	public ReadWebPageException(String detailMessage) {
		super(detailMessage);
	}

	public ReadWebPageException(Throwable cause) {
		super(cause);
	}
}
