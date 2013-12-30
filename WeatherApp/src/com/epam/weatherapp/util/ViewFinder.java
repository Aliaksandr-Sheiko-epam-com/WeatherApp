package com.epam.weatherapp.util;

import com.epam.weatherapp.exception.TechnicalException;

import android.view.View;

public final class ViewFinder {
    private ViewFinder() {}

    // FIXME: use template method here to be able to use constructions like
    // TextView locationView = ViewFinder.findViewByTag()
    // to make type transformation here
    public static View findViewByTag(String tagName, View rootView) throws TechnicalException {
        View view = rootView.findViewWithTag(tagName);
        if (view == null) {
            throw new TechnicalException("No element tag " + tagName);
        }
        return rootView.findViewWithTag(tagName);
    }

}
