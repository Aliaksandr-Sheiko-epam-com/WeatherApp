package com.epam.weatherapp.util.pageloader;

import com.epam.weatherapp.exception.ReadWebPageException;

public interface IPageDownloader {
	String downloadUrl(String url) throws ReadWebPageException;
}
