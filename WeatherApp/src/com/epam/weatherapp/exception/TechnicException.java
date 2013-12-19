package com.epam.weatherapp.exception;

public class TechnicException extends Exception {

	private static final long serialVersionUID = 958974587777144017L;

	public TechnicException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
	}

	public TechnicException(String detailMessage) {
		super(detailMessage);
	}

	public TechnicException(Throwable cause) {
		super(cause);
	}
	
	
}
