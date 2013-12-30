package com.epam.weatherapp.util.uidecoration;

import com.epam.weatherapp.exception.TechnicalException;

public interface IResultViewer {
    void view(Object resultData) throws TechnicalException;
}
