package com.example.bobo_hello.UI.SideNavigationItems.Home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bobo_hello.UI.SideNavigationItems.History.HistoryRecyclerAdapter;
import com.example.bobo_hello.Utils.Classifier;
import com.example.bobo_hello.R;
import com.example.bobo_hello.WeatherServices.WeatherInfoContainer;

public class WeatherInfoFragment extends Fragment {
    private TextView cityNameTextView, tempTextView, windTextView;
    private ImageView weatherImgView, windDirectionImg;
    private final Classifier CLASSIFIRE = new Classifier();
    private RecyclerView recyclerView;


    static WeatherInfoFragment create(WeatherInfoContainer[] weatherContainer) {
        WeatherInfoFragment fragment = new WeatherInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable("index", weatherContainer);
        fragment.setArguments(args);
        return fragment;
    }

    String getCityName(WeatherInfoContainer weatherInfoContainer) {
        try {
            assert weatherInfoContainer != null;
            return weatherInfoContainer.cityName;
        } catch (Exception e) {
            return "";
        }
    }
    String getCurrTemp(WeatherInfoContainer weatherInfoContainer) {
        assert weatherInfoContainer != null;
        if (weatherInfoContainer.isTempOn){
            return weatherInfoContainer.temperature;
        } else return "";
    }

    String getCurrWindSpeed(WeatherInfoContainer weatherInfoContainer) {
        assert weatherInfoContainer != null;
        if (weatherInfoContainer.isWindOn){
            return weatherInfoContainer.windSpeed + " m/s";
        } else return "";
    }

    String getCurrIcon(WeatherInfoContainer weatherInfoContainer) {
        try {
            assert weatherInfoContainer != null;
            String str = weatherInfoContainer.icon;
            return str.toLowerCase();
        } catch (Exception e) {
            return "";
        }
    }

    int getCurrWindDirection(WeatherInfoContainer weatherInfoContainer) {
        assert weatherInfoContainer != null;
        return weatherInfoContainer.windDirection;
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
    }

    @Override
    public void onStart() {
        super.onStart();
        setCurrWeatherInfo();
    }

    private void initViews(View view) {
        cityNameTextView = view.findViewById(R.id.chosenCityName);
        tempTextView = view.findViewById(R.id.tempText);
        windTextView = view.findViewById(R.id.windSpeedText);
        weatherImgView = view.findViewById(R.id.weatherImg);
        windDirectionImg = view.findViewById(R.id.windDirectionIndicator);
        recyclerView = view.findViewById(R.id.daily_weather_list);
    }

    private void setupRecycler(WeatherInfoContainer[] weatherInfoContainer) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        DailyWeatherRecyclerAdapter adapter = new DailyWeatherRecyclerAdapter(weatherInfoContainer, CLASSIFIRE);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void setCurrWeatherInfo(){
        WeatherInfoContainer[] weatherInfoContainer = (WeatherInfoContainer[]) (requireArguments()
                .getSerializable("index"));
        setupRecycler(weatherInfoContainer);
        cityNameTextView.setText(getCityName(weatherInfoContainer[0]));
        tempTextView.setText(getCurrTemp(weatherInfoContainer[0]));
        windTextView.setText(getCurrWindSpeed(weatherInfoContainer[0]));
        setCurrWeatherImg(weatherInfoContainer[0]);
        setCurrWindImg(weatherInfoContainer[0]);
    }

    private void setCurrWeatherImg(WeatherInfoContainer weatherInfoContainer) {
        String iconName = CLASSIFIRE.getIconName(getCurrIcon(weatherInfoContainer));
        int id = getResources().getIdentifier("com.example.bobo_hello:drawable/" + iconName, null, null);
        weatherImgView.setImageResource(id);
    }

    private void setCurrWindImg(WeatherInfoContainer weatherInfoContainer){
        int windDirection = getCurrWindDirection(weatherInfoContainer);
        windDirectionImg.setRotation(windDirection);
    }
}