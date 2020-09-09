package com.example.bobo_hello.Utils;

import java.io.Serializable;

public class WeatherInfoContainer implements Serializable {
    public int position = 0;
    public String cityName = "";
    public String temperature = "";
    public String windSpeed = "";
    public int windDirection = 0;
    public String icon = "";
    public boolean isTempOn = true;
    public boolean isWindOn = true;
    public Classifier classifier;
}
