package com.epam.weatherapp.util;

import com.epam.weatherapp.exception.TechnicalException;

import android.view.View;

public class ViewFinder<T extends View> {
    private View rootView;
    
    public ViewFinder(View rootView) {
        this.rootView = rootView;
    }

    @SuppressWarnings("unchecked")
    public T findViewByTag(String tagName) throws TechnicalException  {
        View view = rootView.findViewWithTag(tagName);
        if (view == null) {
            throw new TechnicalException("No element tag " + tagName);
        }
        return (T) rootView.findViewWithTag(tagName);
    }

}
