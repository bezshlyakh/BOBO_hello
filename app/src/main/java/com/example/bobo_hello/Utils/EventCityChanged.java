package com.example.bobo_hello.Utils;

import com.google.android.gms.maps.model.LatLng;

public class EventCityChanged {
    public final LatLng cityCoord;

    public EventCityChanged(LatLng coordinates) {
        this.cityCoord = coordinates;
    }
}
