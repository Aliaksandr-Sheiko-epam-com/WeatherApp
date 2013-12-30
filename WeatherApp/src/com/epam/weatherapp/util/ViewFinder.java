package com.epam.weatherapp.util;

import android.view.View;

import com.epam.weatherapp.exception.TechnicalException;

public final class ViewFinder {
    private ViewFinder() {}
    
    public static View findViewByTag(String tagName, View rootView) throws TechnicalException {
        View view = rootView.findViewWithTag(tagName);
        if (view == null) {
            throw new TechnicalException("No element tag " + tagName);
        }
        return rootView.findViewWithTag(tagName);
    }

}
