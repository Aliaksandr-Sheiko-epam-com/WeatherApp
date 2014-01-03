package com.epam.weatherapp.util.uidecoration;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.epam.weatherapp.exception.TechnicalException;
import com.epam.weatherapp.model.LocationInfo;
import com.epam.weatherapp.util.ViewFinder;

public class ChooseLocationDecorator implements IUIDecorator {

    @Override
    public void showDecoration(View rootView) throws TechnicalException {
        throw new UnsupportedOperationException(AvailibleLocationDecorator.class.getName()
            + " doesn't support showDecoration method");
    }

    @Override
    public IResultViewer createAfterTaskDecorator(View rootView) {
        return new AvailibleLocationDecorator(rootView);
    }

    private class AvailibleLocationDecorator implements IResultViewer {
        private static final String VIEW_TAG = "auto_complete_text_view";
        private View rootView;

        public AvailibleLocationDecorator(View rootView) {
            this.rootView = rootView;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void view(Object resultData) throws TechnicalException {
            List<LocationInfo> locationList = (List<LocationInfo>) resultData;
            ViewFinder<AutoCompleteTextView> viewFinder = new ViewFinder<AutoCompleteTextView>(rootView);
            AutoCompleteTextView autoCompleteTextView = viewFinder.findViewByTag(VIEW_TAG);
            Context context = autoCompleteTextView.getContext();
            ArrayAdapter<LocationInfo> adapter = new ArrayAdapter<LocationInfo>(context, android.R.layout.simple_list_item_1,
                locationList);
            autoCompleteTextView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }

}
