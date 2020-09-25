package com.example.bobo_hello.Utils;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;

import com.example.bobo_hello.MainActivity;
import com.google.android.gms.maps.model.LatLng;
import java.io.IOException;
import java.util.List;

public class CoordConverter {
    private LatLng coordinates;
    private String cityName;
    private final Geocoder GEOCODER;

    public CoordConverter(Geocoder geocoder){
        this.GEOCODER = geocoder;
    }

    public String getCityNameByCoord(LatLng target) throws InterruptedException {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<Address> addresses = GEOCODER.getFromLocation(target.latitude, target.longitude, 1);
                    cityName = addresses.get(0).getLocality();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        th.start();
        th.join();
        return cityName;
    }

    public LatLng getCoordinatesByCityName(String name) throws InterruptedException {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<Address> addresses = GEOCODER.getFromLocationName(name, 1);

                    if (addresses.size() > 0) {
                        coordinates = new LatLng(addresses.get(0).getLatitude(),
                                addresses.get(0).getLongitude());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        th.start();
        th.join();
        return coordinates;
    }
}

