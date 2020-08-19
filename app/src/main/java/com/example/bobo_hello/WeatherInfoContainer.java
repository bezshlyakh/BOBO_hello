package com.example.bobo_hello;

import java.io.Serializable;

public class WeatherInfoContainer implements Serializable {
    public int position = 0;
    public String cityName = "";
    public boolean isTempOn = true;
    public boolean isWindOn = true;
}
