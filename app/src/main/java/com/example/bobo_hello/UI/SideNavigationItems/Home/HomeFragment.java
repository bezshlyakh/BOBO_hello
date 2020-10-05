package com.example.bobo_hello.UI.SideNavigationItems.Home;

import android.annotation.SuppressLint;
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
import com.example.bobo_hello.AppForDB;
import com.example.bobo_hello.R;
import com.example.bobo_hello.UI.SideNavigationItems.History.CityEntity;
import com.example.bobo_hello.UI.SideNavigationItems.History.TemperatureEntity;
import com.example.bobo_hello.UI.SideNavigationItems.History.WeatherHisSource;
import com.example.bobo_hello.UI.SideNavigationItems.History.WeatherHistoryDao;
import com.example.bobo_hello.Utils.CoordConverter;
import com.example.bobo_hello.Utils.EventCityChanged;
import com.example.bobo_hello.WeatherServices.WeatherInfoContainer;
import com.example.bobo_hello.WeatherServices.WeatherModelHandler;
import com.google.android.gms.maps.model.LatLng;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    private final float DEFAULT_LATITUDE = 55.75f;
    private final float DEFAULT_LONGITUDE = 37.62f;
    private LatLng currentCoord;
    private WeatherModelHandler weatherModelHandler;
    private WeatherInfoContainer weatherInfoContainer;
    private ProgressBar progressBar;
    private CoordConverter converter;
//    private WeatherHisSource weatherHisSource;
//    private CityEntity cityEntity;
//    private TemperatureEntity temperatureEntity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initGUI(view);
//        initDBHandlers();
        weatherModelHandler = new WeatherModelHandler();
        weatherInfoContainer = new WeatherInfoContainer();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Geocoder geocoder = new Geocoder(requireContext());
        converter = new CoordConverter(geocoder);
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
                    try {
                        weatherInfoContainer.cityName = converter.getCityNameByCoord(coord);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
//        saveWeatherInfoToDB(weatherInfoContainer);
        progressBar.setVisibility(View.GONE);
        return WeatherInfoFragment.create(weatherInfoContainer);
    }

    private WeatherInfoContainer getWeatherInfoFromServer(LatLng target) throws IOException {
        return weatherModelHandler.getOneDayWeatherContainer((float)target.latitude, (float)target.longitude);
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

//    private void initDBHandlers(){
//        WeatherHistoryDao weatherHistoryDao = AppForDB
//                .getInstance()
//                .getWeatherDao();
//        this.weatherHisSource = new WeatherHisSource(weatherHistoryDao);
//        this.cityEntity = new CityEntity();
//        this.temperatureEntity = new TemperatureEntity();
//    }

//    private void saveWeatherInfoToDB(WeatherInfoContainer connector){
//        cityEntity.cityName = connector.cityName;
//        weatherHisSource.addCity(cityEntity);
//        temperatureEntity.cityId = cityEntity.id;
//        temperatureEntity.temperature = connector.temperature;
//        temperatureEntity.date = getCurrentTime();
//        weatherHisSource.addTemp(temperatureEntity);
//    }
//
//    private String getCurrentTime(){
//        @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("MMM dd yyyy, h:mm");
//        return date.format(Calendar.getInstance().getTime());
//    }



//    private void setOptions(){
//        tempOn = readFromPreference().getBoolean(IS_TEMP_ON, true); // изменить состав опций - влажность, направление ветра, фарингейты
//        windOn = readFromPreference().getBoolean(IS_WIND_ON, true);
//    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.homeFragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
