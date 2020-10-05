package com.example.bobo_hello.WeatherServices;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.bobo_hello.AppForDB;
import com.example.bobo_hello.UI.SideNavigationItems.History.CityEntity;
import com.example.bobo_hello.UI.SideNavigationItems.History.TemperatureEntity;
import com.example.bobo_hello.UI.SideNavigationItems.History.WeatherHisSource;
import com.example.bobo_hello.UI.SideNavigationItems.History.WeatherHistoryDao;
import com.example.bobo_hello.WeatherModels.OneDayModel.WeatherRequest;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Response;

public class WeatherModelHandler {

    private static final String API_KEY = "3aa391c929cfb72a03bebcdf69fc4c8a";
    private final String UNITS = "metric";
    private final String TAG = "WeatherHandler";
    private WeatherInfoContainer oneDayWeatherContainer;
    private WeatherHisSource weatherHisSource;
    private CityEntity cityEntity;
    private TemperatureEntity temperatureEntity;

    //private WeatherInfoContainer threeDayWeatherContainer = new WeatherInfoContainer(); //todo api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={API key}


    public WeatherModelHandler() {
        initDBHandlers();
    }

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
        oneDayWeatherContainer.temperature = (int) rawModel.getMain().getTemp() + " °C";
        oneDayWeatherContainer.windSpeed = String.valueOf(rawModel.getWind().getSpeed());
        oneDayWeatherContainer.windDirection = rawModel.getWind().getDeg();
        oneDayWeatherContainer.icon = String.valueOf(rawModel.getWeather()[0].getIcon());
        return oneDayWeatherContainer;
    }

    public WeatherInfoContainer getOneDayWeatherContainer(float lat, float lon) throws IOException {
        updateOneDayWeatherData(lat, lon);
        saveWeatherInfoToDB(oneDayWeatherContainer);
        return oneDayWeatherContainer;
    }

    private void initDBHandlers(){
        WeatherHistoryDao weatherHistoryDao = AppForDB
                .getInstance()
                .getWeatherDao();
        this.weatherHisSource = new WeatherHisSource(weatherHistoryDao);
        this.cityEntity = new CityEntity();
        this.temperatureEntity = new TemperatureEntity();
    }

    private void saveWeatherInfoToDB(WeatherInfoContainer connector){
        cityEntity.cityName = connector.cityName;
        weatherHisSource.addCity(cityEntity);
        temperatureEntity.cityId = weatherHisSource.getCityEntityID();
        temperatureEntity.temperature = connector.temperature;
        temperatureEntity.date = getCurrentTime();
        weatherHisSource.addTemp(temperatureEntity);
    }

    private String getCurrentTime(){
        @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("MMM dd yyyy, HH:mm");
        return date.format(Calendar.getInstance().getTime());
    }
}
