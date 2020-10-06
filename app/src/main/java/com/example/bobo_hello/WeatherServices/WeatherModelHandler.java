package com.example.bobo_hello.WeatherServices;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.bobo_hello.AppForDB;
import com.example.bobo_hello.UI.SideNavigationItems.History.CityEntity;
import com.example.bobo_hello.UI.SideNavigationItems.History.TemperatureEntity;
import com.example.bobo_hello.UI.SideNavigationItems.History.WeatherHisSource;
import com.example.bobo_hello.UI.SideNavigationItems.History.WeatherHistoryDao;
import com.example.bobo_hello.WeatherModels.DailyForecastModel.WeatherRequest;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Response;

public class WeatherModelHandler {

    private static final String API_KEY = "3aa391c929cfb72a03bebcdf69fc4c8a";
    private final String units;
    private final String defaultMetric = "metric";
    private final String celcMetric = " °C";
    private final String farinMetric = " °F";
    private final String TAG = "WeatherHandler";
    private final int CNT = 15;
    private WeatherInfoContainer[] weatherContainer;
    private WeatherHisSource weatherHisSource;
    private CityEntity cityEntity;
    private TemperatureEntity temperatureEntity;

    public WeatherModelHandler(String units) {
        this.units = units;
        initDBHandlers();
    }

    private void updateOneDayWeatherData(float lat, float lon) throws IOException {
        Response<com.example.bobo_hello.WeatherModels.DailyForecastModel.WeatherRequest> response = UpdateWeatherRepo.getInstance().getAPI().loadWeather(lat, lon, CNT, API_KEY, units)
                .execute();
        if (response.body() != null && response.isSuccessful()) {
            weatherContainer = buildWeatherContainer(response.body());
        } else {
            if(response.code() == 500) {
                Log.d(TAG, "connection error " + response.code());
            } else if(response.code() == 401) {
                Log.d(TAG, "auth error " + response.code());
            }
        }
    }

    private WeatherInfoContainer[] buildWeatherContainer(WeatherRequest rawModel) {
        weatherContainer = new WeatherInfoContainer[rawModel.getListSize()];

        for(int i = 0; i < weatherContainer.length; i++){
            weatherContainer[i] = new WeatherInfoContainer();
        }

        for (int i = 0; i < weatherContainer.length; i++) {
            weatherContainer[i].cityName = rawModel.getCity().getName();
            weatherContainer[i].date = getDate(rawModel.getWeatherList()[i].getDt());

            String metric;
            if(units.equals(defaultMetric)){
                metric = celcMetric;
            } else metric = farinMetric;
            weatherContainer[i].temperature = (int) rawModel.getWeatherList()[i].getMain().getTemp() + metric;

            weatherContainer[i].windSpeed = String.valueOf(rawModel.getWeatherList()[i].getWind().getSpeed());
            weatherContainer[i].windDirection = rawModel.getWeatherList()[i].getWind().getDeg();
            weatherContainer[i].icon = String.valueOf(rawModel.getWeatherList()[i].getWeather()[0].getIcon());
        }

        return weatherContainer;
    }

    public WeatherInfoContainer[] getWeatherContainer(float lat, float lon) throws IOException {
        updateOneDayWeatherData(lat, lon);
        saveWeatherInfoToDB(weatherContainer[0]);
        return weatherContainer;
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

    private String getDate(long dt){
        @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("MMM dd yyyy, HH:mm");
        return date.format(new Date(dt * 1000));
    }

}
