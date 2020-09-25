package com.example.bobo_hello.WeatherServices;

import android.util.Log;
import com.example.bobo_hello.weatherModel.WeatherRequest;
import java.io.IOException;
import retrofit2.Response;

public class WeatherModelHandler {

    private static final String API_KEY = "3aa391c929cfb72a03bebcdf69fc4c8a";
    private final String UNITS = "metric";
    private final String TAG = "WeatherHandler";
    private WeatherInfoContainer oneDayWeatherContainer;

    //private WeatherInfoContainer threeDayWeatherContainer = new WeatherInfoContainer(); //todo

    private void updateOneDayWeatherData(float lat, float lon) throws IOException {
        Response<WeatherRequest> response = UpdateWeatherRepo.getInstance().getAPI().loadWeather(lat, lon, API_KEY, UNITS)
                .execute();
        if (response.body() != null && response.isSuccessful()) {
            oneDayWeatherContainer = buildOneDayWeatherContainer(response.body());
        } else {
            if(response.code() == 500) {
                Log.d(TAG, "connection error " + response.code());
            } else if(response.code() == 401) {
                Log.d(TAG, "auth error " + response.code());
            }
        }
    }

    private WeatherInfoContainer buildOneDayWeatherContainer(WeatherRequest rawModel){
        WeatherInfoContainer oneDayWeatherContainer = new WeatherInfoContainer();
        oneDayWeatherContainer.cityName = rawModel.getName();
        oneDayWeatherContainer.temperature = String.valueOf((int)rawModel.getMain().getTemp());
        oneDayWeatherContainer.windSpeed = String.valueOf(rawModel.getWind().getSpeed());
        oneDayWeatherContainer.windDirection = rawModel.getWind().getDeg();
        oneDayWeatherContainer.icon = String.valueOf(rawModel.getWeather()[0].getIcon());
        return oneDayWeatherContainer;
    }

    public WeatherInfoContainer getOneDayWeatherContainer(float lat, float lon) throws IOException {
        updateOneDayWeatherData(lat, lon);
        return oneDayWeatherContainer;
    }

}
