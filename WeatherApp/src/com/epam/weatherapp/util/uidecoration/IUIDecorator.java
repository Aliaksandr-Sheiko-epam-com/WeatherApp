package com.epam.weatherapp.util.uidecoration;

import com.epam.weatherapp.exception.TechnicalException;

import android.view.View;

public interface IUIDecorator {
    void showDecoration(View rootView) throws TechnicalException;
    
    IResultViewer createAfterTaskDecorator(View rootView);
}
