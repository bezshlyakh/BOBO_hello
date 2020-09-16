package com.example.bobo_hello.UI.SideNavigationItems.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.bobo_hello.R;
import com.example.bobo_hello.Utils.Classifier;
import com.example.bobo_hello.Utils.EventCityChanged;
import com.example.bobo_hello.Utils.OneDayWeatherConnector;
import com.example.bobo_hello.Utils.WeatherInfoContainer;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HomeFragment extends Fragment {

    private final float DEFAULT_LATITUDE = 55.75f;
    private final float DEFAULT_LONGITUDE = 37.62f;
    private Classifier classifier;
    private LatLng currentCoord;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        classifier = new Classifier();
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

    private WeatherInfoFragment updateWeatherFrag(LatLng coord){
        return WeatherInfoFragment.create(getWeatherContainer(getWeatherInfoFromServer(coord)));
    }

    private WeatherInfoContainer getWeatherContainer(OneDayWeatherConnector connector) {
        WeatherInfoContainer container = new WeatherInfoContainer();
        container.cityName = connector.getCityName();
        container.temperature = connector.getTemperature();
        container.windSpeed = connector.getWindSpeed();
        container.windDirection = connector.getWindDirection();
        container.icon = connector.getIcon();
        container.classifier = classifier;
        return container;
    }

    private OneDayWeatherConnector getWeatherInfoFromServer(LatLng target) {
        return new OneDayWeatherConnector(target);
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
