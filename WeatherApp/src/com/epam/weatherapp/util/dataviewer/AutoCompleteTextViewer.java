package com.epam.weatherapp.util.dataviewer;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.epam.weatherapp.exception.TechnicalException;
import com.epam.weatherapp.model.LocationInfo;
import com.epam.weatherapp.util.ViewFinder;

public final class AutoCompleteTextViewer implements ILocationListViewer {
    public static final String VIEW_TAG = "auto_complete_text_view";
    private View rootView;

    public AutoCompleteTextViewer(View rootView) {
        this.rootView = rootView;
    }

    @Override
    public void view(List<LocationInfo> locationList) throws TechnicalException {
        ViewFinder<AutoCompleteTextView> viewFinder = new ViewFinder<AutoCompleteTextView>(rootView);
        AutoCompleteTextView autoCompleteTextView = viewFinder.findViewByTag(VIEW_TAG);
        Context context = autoCompleteTextView.getContext();
        ArrayAdapter<LocationInfo> adapter = new ArrayAdapter<LocationInfo>(context,
            android.R.layout.simple_list_item_1, locationList);
        autoCompleteTextView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
