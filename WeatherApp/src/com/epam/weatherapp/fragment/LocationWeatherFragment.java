package com.epam.weatherapp.fragment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.epam.weatherapp.R;
import com.epam.weatherapp.exception.TechnicalException;
import com.epam.weatherapp.model.LocationInfo;
import com.epam.weatherapp.util.dataviewer.DisplayLocationWeatherTask;
import com.epam.weatherapp.util.uidecoration.IUIDecorator;
import com.epam.weatherapp.util.uidecoration.WeatherDisplayDecorator;

public class LocationWeatherFragment extends Fragment {
    public final static String LOCATION_INFO = "com.epam.weatherapp.activity.LOCATION_INFO";
    public static final String ARG_PAGE = "page";
    private static final String LOCATION_URL = "http://apidev.accuweather.com/currentconditions/v1/%s.json?language=en&apikey=hAilspiKe";
    private ExecutorService pool;
    private IUIDecorator uiDecorator;
    private View rootView;
    private boolean isReloadData = true;
    private int mPageNumber;
    
    public static LocationWeatherFragment create(int pageNumber) {
        LocationWeatherFragment fragment = new LocationWeatherFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiDecorator = new WeatherDisplayDecorator(getLocationInfo());
        mPageNumber = getArguments().getInt(ARG_PAGE);
        pool = Executors.newFixedThreadPool(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (rootView != null) {
            return detachFromParent(rootView);
        }
        else {
            return rootView = inflater.inflate(R.layout.weather_view, container, false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isReloadData) {
            try {
                isReloadData = false;
                uiDecorator.showDecoration(rootView);
                String url = String.format(LOCATION_URL, getCountryKey());
                pool.execute(new DisplayLocationWeatherTask(rootView, url, uiDecorator.createAfterTaskDecorator(rootView)));
            }
            catch (TechnicalException e) {
                Toast.makeText(getActivity(), "Can't display weather for given key", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    public int getPageNumber() {
        return mPageNumber;
    }

    private LocationInfo getLocationInfo() {
        Bundle args = getArguments();
        if (args != null) {
            return (LocationInfo) args.getSerializable(LOCATION_INFO);
        }
        throw new IllegalStateException("Location info wasn't resived from activity");
    }

    private String getCountryKey() throws TechnicalException {
        return getLocationInfo().getKey();
    }

    private View detachFromParent(View view) {
        ViewGroup parent = (ViewGroup) rootView.getParent();
        parent.removeView(rootView);
        return view;
    }
}
