package com.epam.weatherapp.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

//remade to another activity
public final class LocationListFragment extends ListFragment {
    //FIXME: package friendly field here?
    OnHeadlineSelectedListener mCallback;

    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(int position);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundColor(Color.WHITE);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        //FIXME: you'd better firstly check using instanceof or something instead of using try\catch and
        // than throw illegal exception here
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
        }
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? android.R.layout.simple_list_item_activated_1
            : android.R.layout.simple_list_item_1;
        setListAdapter(new ArrayAdapter<String>(getActivity(), layout, new String[] { 
            "Article One",
            "Article Two"
        }));
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mCallback.onArticleSelected(position);
        getListView().setItemChecked(position, true);
    }
    
}