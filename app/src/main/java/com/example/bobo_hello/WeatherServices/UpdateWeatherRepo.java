package com.example.bobo_hello.WeatherServices;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateWeatherRepo {
    private static UpdateWeatherRepo instance = null;
    private IUpdateWeather API;

    private UpdateWeatherRepo() {
        API = createAdapter();
    }

    public static UpdateWeatherRepo getInstance() {
        if(instance == null) {
            instance = new UpdateWeatherRepo();
        }

        return instance;
    }

    public IUpdateWeather getAPI() {
        return API;
    }

    private IUpdateWeather createAdapter() {
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return adapter.create(IUpdateWeather.class);
    }
}
