package com.example.bobo_hello.WeatherModels.DailyForecastModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherRequest {
    @SerializedName("cod")
    @Expose
    private String cod;

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    @SerializedName("message")
    @Expose
    private int message;

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    @SerializedName("cnt")
    @Expose
    private int cnt;


    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    @SerializedName("list")
    @Expose
    private WeatherList[] list;

    public WeatherList[] getWeatherList() {
        return list;
    }

    public void setWeatherList(WeatherList[] list) {
        this.list = list;
    }

    public int getListSize(){
        return list.length;
    }

    @SerializedName("city")
    @Expose
    private City city;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
