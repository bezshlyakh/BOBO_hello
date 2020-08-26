package com.example.bobo_hello;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Classifier implements Serializable {
    private HashMap<String, String> citiesIDBase;
    private HashMap<String, String> weatherImgBase;


    public Classifier(){
        citiesIDBase = new HashMap<>();
        citiesIDBase.put("Moscow", "524901");
        citiesIDBase.put("Saint Petersburg", "498817");
        citiesIDBase.put("Tomsk", "1489425");
        citiesIDBase.put("Vladivostok", "2013348");
        citiesIDBase.put("Kyiv", "703448");
        citiesIDBase.put("Minsk", "625144");
        citiesIDBase.put("Berlin", "2950159");

        weatherImgBase = new HashMap<>();

        weatherImgBase.put("01d", "clear_sky"); //light theme
        weatherImgBase.put("02d", "few_clouds");
        weatherImgBase.put("03d", "clouds");
        weatherImgBase.put("04d", "clouds");
        weatherImgBase.put("09d", "shower_rain");
        weatherImgBase.put("10d", "rain");
        weatherImgBase.put("11d", "thunder_storm");
        weatherImgBase.put("13d", "snow");
        weatherImgBase.put("50d", "mist");

        weatherImgBase.put("01n", "clear_sky"); //night theme
        weatherImgBase.put("02n", "few_clouds");
        weatherImgBase.put("03n", "clouds");
        weatherImgBase.put("04n", "clouds");
        weatherImgBase.put("09n", "shower_rain");
        weatherImgBase.put("10n", "rain");
        weatherImgBase.put("11n", "thunder_storm");
        weatherImgBase.put("13n", "snow");
        weatherImgBase.put("50n", "mist");
    }

    public String getCityID(String cityName){
        return citiesIDBase.get(cityName);
    }
    public String getIconName(String icon){
        return weatherImgBase.get(icon);
    }
    public ArrayList<String> getListOfCities(){
        citiesIDBase.keySet();
        Set<String> listOfKeys = citiesIDBase.keySet();
        return new ArrayList<>(listOfKeys);
    }

}
