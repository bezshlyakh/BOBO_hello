package com.example.bobo_hello.UI.SideNavigationItems.Home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.bobo_hello.Utils.Classifier;
import com.example.bobo_hello.R;
import com.example.bobo_hello.Utils.WeatherInfoContainer;

import java.util.Objects;

public class WeatherInfoFragment extends Fragment {
    private TextView cityNameTextView, tempTextView, windTextView;
    private ImageView weatherImgView;

    static WeatherInfoFragment create(WeatherInfoContainer weatherContainer) {
        WeatherInfoFragment fragment = new WeatherInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable("index", weatherContainer);
        fragment.setArguments(args);
        return fragment;
    }

    int getIndex() {
        WeatherInfoContainer weatherInfoContainer = (WeatherInfoContainer) (Objects.requireNonNull(getArguments())
                .getSerializable("index"));
        try {
            assert weatherInfoContainer != null;
            return weatherInfoContainer.position;
        } catch (Exception e) {
            return 0;
        }
    }

    String getCityName() {
        WeatherInfoContainer weatherInfoContainer = (WeatherInfoContainer) (Objects.requireNonNull(getArguments())
                .getSerializable("index"));
        try {
            assert weatherInfoContainer != null;
            return weatherInfoContainer.cityName;
        } catch (Exception e) {
            return "";
        }
    }
    String getTemperature() {
        WeatherInfoContainer weatherInfoContainer = (WeatherInfoContainer) (Objects.requireNonNull(getArguments())
                .getSerializable("index"));
        assert weatherInfoContainer != null;
        if (weatherInfoContainer.isTempOn){
            return weatherInfoContainer.temperature + " °C";
        } else return "";
    }

    String getWindSpeed() {
        WeatherInfoContainer weatherInfoContainer = (WeatherInfoContainer) (Objects.requireNonNull(getArguments())
                .getSerializable("index"));
        assert weatherInfoContainer != null;
        if (weatherInfoContainer.isWindOn){
            return weatherInfoContainer.windSpeed + " m/s";
        } else return "";
    }

    String getIcon() {
        WeatherInfoContainer weatherInfoContainer = (WeatherInfoContainer) (Objects.requireNonNull(getArguments())
                .getSerializable("index"));
        try {
            assert weatherInfoContainer != null;
            String str = weatherInfoContainer.icon;
            return str.toLowerCase();
        } catch (Exception e) {
            return "";
        }
    }

    Classifier getClassifier() {
        WeatherInfoContainer weatherInfoContainer = (WeatherInfoContainer) (Objects.requireNonNull(getArguments())
                .getSerializable("index"));
        try {
            assert weatherInfoContainer != null;
            return weatherInfoContainer.classifier;
        } catch (Exception e) {
            return null;
        }
    }
    @Override
    @SuppressLint("Recycle")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setWeatherInfo();
    }

    private void initViews(View view) {
        cityNameTextView = view.findViewById(R.id.chosenCityName);
        tempTextView = view.findViewById(R.id.tempText);
        windTextView = view.findViewById(R.id.windSpeedText);
        weatherImgView = view.findViewById(R.id.weatherImg);
    }

    private void setWeatherInfo(){
        cityNameTextView.setText(getCityName());
        tempTextView.setText(getTemperature());
        windTextView.setText(getWindSpeed());
        setWeatherImg();
    }

    private void setWeatherImg() {
        String iconName = getClassifier().getIconName(getIcon());
        int id = getResources().getIdentifier("com.example.bobo_hello:drawable/" + iconName, null, null);
        weatherImgView.setImageResource(id);
    }
}