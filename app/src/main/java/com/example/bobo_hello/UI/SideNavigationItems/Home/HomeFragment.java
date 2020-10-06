package com.example.bobo_hello.UI.SideNavigationItems.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.bobo_hello.R;
import com.example.bobo_hello.Utils.CoordConverter;
import com.example.bobo_hello.Utils.EventCityChanged;
import com.example.bobo_hello.WeatherServices.WeatherInfoContainer;
import com.example.bobo_hello.WeatherServices.WeatherModelHandler;
import com.google.android.gms.maps.model.LatLng;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.IOException;

public class HomeFragment extends Fragment {

    private final String METRIC_CHOSEN = "metricChosen";
    private final String defaultMetric = "metric";
    private final float DEFAULT_LATITUDE = 55.75f;
    private final float DEFAULT_LONGITUDE = 37.62f;
    private LatLng currentCoord;
    private WeatherModelHandler weatherModelHandler;
    private WeatherInfoContainer[] weatherInfoContainer;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initGUI(view);
        weatherModelHandler = new WeatherModelHandler(readFromPreference().getString(METRIC_CHOSEN, defaultMetric));
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        currentCoord = new LatLng( readFromPreference().getFloat("currentLatitude", DEFAULT_LATITUDE),
                readFromPreference().getFloat("currentLongitude", DEFAULT_LONGITUDE));
        setFragment(updateWeatherFrag(currentCoord));
    }

    @Override
    public void onResume() {
        super.onResume();
        currentCoord = new LatLng( readFromPreference().getFloat("currentLatitude", DEFAULT_LATITUDE),
                readFromPreference().getFloat("currentLongitude", DEFAULT_LONGITUDE));
        setFragment(updateWeatherFrag(currentCoord));
    }

    private void initGUI(View view){
        progressBar = view.findViewById(R.id.loadingProgressBar);
    }

    private WeatherInfoFragment updateWeatherFrag(LatLng coord){
        progressBar.setVisibility(View.VISIBLE);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    weatherInfoContainer = getWeatherInfoFromServer(coord);
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
        progressBar.setVisibility(View.GONE);
        return WeatherInfoFragment.create(weatherInfoContainer);
    }

    private WeatherInfoContainer[] getWeatherInfoFromServer(LatLng target) throws IOException {
        return weatherModelHandler.getWeatherContainer((float)target.latitude, (float)target.longitude);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
        public void onCityChanged(EventCityChanged eventCityChanged){
        setFragment(updateWeatherFrag(eventCityChanged.cityCoord));
    }

    private SharedPreferences readFromPreference() {
        return requireActivity().getPreferences(Context.MODE_PRIVATE);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.homeFragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
