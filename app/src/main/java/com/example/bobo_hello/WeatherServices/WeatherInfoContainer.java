package com.example.bobo_hello.WeatherServices;

import com.example.bobo_hello.Utils.Classifier;

import java.io.Serializable;

public class WeatherInfoContainer implements Serializable {
    public String cityName = "";
    public String date = "";
    public String temperature = "";
    public String windSpeed = "";
    public int windDirection = 0;
    public String icon = "";
    public boolean isTempOn = true;
    public boolean isWindOn = true;
}
