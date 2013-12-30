package com.epam.weatherapp.util.dataviewer;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.epam.weatherapp.model.LocationInfo;

public final class AutoCompleteTextViewer implements ILocationListViewer {
    public static final String VIEW_TAG = "auto_complete_text_view";
    private View rootView;

    public AutoCompleteTextViewer(View rootView) {
        this.rootView = rootView;
    }

    @Override
    public void view(List<LocationInfo> locationList) {
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewByTag(VIEW_TAG);
        Context context = autoCompleteTextView.getContext();
        ArrayAdapter<LocationInfo> adapter = new ArrayAdapter<LocationInfo>(context,
            android.R.layout.simple_list_item_1, locationList);
        autoCompleteTextView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    
    private View findViewByTag(String tagName) {
        return rootView.findViewWithTag(tagName);
    }
}
