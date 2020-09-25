package com.example.bobo_hello.UI.SideNavigationItems.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.bobo_hello.R;
import com.example.bobo_hello.Utils.EventCityChanged;
import com.example.bobo_hello.WeatherServices.WeatherModelHandler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap = null;
    private LatLng currLocation;
    private String currCityTemp;
    private final float DEFAULT_LATITUDE = 55.75f;
    private final float DEFAULT_LONGITUDE = 37.62f;
    private TextView tempTV;
    private WeatherModelHandler weatherModelHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.fragment_map, null, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_view);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        LocationManager mLocManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
//        requestPermissions();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        weatherModelHandler = new WeatherModelHandler();
    }

    private void initViews(View view){
        tempTV = view.findViewById(R.id.tempOnMapTV);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        currLocation = new LatLng( readFromPreference().getFloat("currentLatitude", DEFAULT_LATITUDE),
                readFromPreference().getFloat("currentLongitude", DEFAULT_LONGITUDE));
    }

    @Override
    public void onResume() {
        super.onResume();
        currLocation = new LatLng( readFromPreference().getFloat("currentLatitude", DEFAULT_LATITUDE),
                readFromPreference().getFloat("currentLongitude", DEFAULT_LONGITUDE));
    }


    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        currLocation = new LatLng( readFromPreference().getFloat("currentLatitude", DEFAULT_LATITUDE),
                readFromPreference().getFloat("currentLongitude", DEFAULT_LONGITUDE));
        toCurrentLocation(currLocation);
        updateTemperatureIndicator(currLocation);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                toCurrentLocation(latLng);
                updateTemperatureIndicator(latLng);
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCityChanged(EventCityChanged eventCityChanged){
        toCurrentLocation(eventCityChanged.cityCoord);
    }


    private void toCurrentLocation(LatLng target) {
        currLocation = target;
        moveCamera(currLocation);
        addMarker(currLocation);
    }

    private void updateTemperatureIndicator(LatLng coord){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    currCityTemp = weatherModelHandler.getOneDayWeatherContainer((float) coord.latitude, (float) coord.longitude).temperature;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String temp = currCityTemp + "°C";
        tempTV.setText(temp);
    }

    private void moveCamera(LatLng target) {
        if (target == null) return;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(target, (float) 10.0));
    }

    private void addMarker(LatLng target){
        mMap.addMarker(new MarkerOptions()
                .position(target));
    }

    private SharedPreferences readFromPreference() {
        return requireActivity().getPreferences(Context.MODE_PRIVATE);
    }
}
