package com.epam.weatherapp.util.pageloader;

import com.epam.weatherapp.exception.TechnicException;

public interface IPageDownloader {
	String downloadUrl(String url) throws TechnicException;
}
