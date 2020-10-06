package com.example.bobo_hello.WeatherServices;

import com.example.bobo_hello.WeatherModels.DailyForecastModel.WeatherRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IUpdateWeather {
    @GET("data/2.5/forecast")
    Call<WeatherRequest> loadWeather(@Query("lat") float latitude,
                                     @Query("lon") float longitude,
                                     @Query("cnt") int timeStamps,
                                     @Query("appid") String keyApi,
                                     @Query("units") String units);

}
