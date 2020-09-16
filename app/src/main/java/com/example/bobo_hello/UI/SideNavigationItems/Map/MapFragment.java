package com.example.bobo_hello.UI.SideNavigationItems.Map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.example.bobo_hello.R;
import com.example.bobo_hello.Utils.EventCityChanged;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap = null;
    private LatLng currLocation;
    private final float DEFAULT_LATITUDE = 55.75f;
    private final float DEFAULT_LONGITUDE = 37.62f;
    //private static final int PERMISSION_REQUEST_CODE = 10;

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
        toCurrentLocation(currLocation);
    }

//    private void requestPermissions() {
//        if (!checkPermissions()) {
//            requestLocationPermissions();
//        }
//    }
//
//    private boolean checkPermissions() {
//        return (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
//    }
//
//    private void requestLocationPermissions() {
//        if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CALL_PHONE)) {
//            ActivityCompat.requestPermissions(requireActivity(),
//                    new String[]{
//                            Manifest.permission.ACCESS_COARSE_LOCATION,
//                            Manifest.permission.ACCESS_FINE_LOCATION
//                    },
//                    PERMISSION_REQUEST_CODE);
//        }
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCityChanged(EventCityChanged eventCityChanged){
        toCurrentLocation(eventCityChanged.cityCoord);
    }


    private void toCurrentLocation(LatLng target) {
        currLocation = target;
        moveCamera(currLocation);
        addMarker(currLocation);
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
