package com.example.bobo_hello.fragments;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bobo_hello.R;
import com.example.bobo_hello.WeatherInfoContainer;

import java.util.Objects;

public class WeatherInfoFragment extends Fragment {
    private TextView cityNameTextView, tempTextView, windTextView;
    private ImageView weatherImgView;
    private boolean tempOn, windOn;

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

    void getOptions(){
        WeatherInfoContainer weatherInfoContainer = (WeatherInfoContainer) (Objects.requireNonNull(getArguments())
                .getSerializable("index"));
            assert weatherInfoContainer != null;
            tempOn = weatherInfoContainer.isTempOn;
            windOn = weatherInfoContainer.isWindOn;
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
        getOptions();
        setTempInfo();
        setWindInfo();
        setWeatherImg();
    }

    private void setWeatherImg() {
        @SuppressLint("Recycle") TypedArray images = getResources().obtainTypedArray(R.array.weather_imgs);
        weatherImgView.setImageResource(images.getResourceId(getIndex(), -1));
    }

    private void setTempInfo() {
        @SuppressLint("Recycle") TypedArray tempArr = getResources().obtainTypedArray(R.array.temperature);
        if(tempOn){
            tempTextView.setText(tempArr.getString(getIndex()));
        } else tempTextView.setText("");
    }

    private void setWindInfo() {
        @SuppressLint("Recycle") TypedArray windArr = getResources().obtainTypedArray(R.array.wind);
        if(windOn){
            windTextView.setText(windArr.getString(getIndex()));
        } else windTextView.setText("");
    }
}