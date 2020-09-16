package com.example.bobo_hello.UI.SideNavigationItems.History;

import java.util.List;

public class WeatherHisSource {

    private final WeatherHistoryDao weatherHistoryDao;
    private List<CityAndTemperatureResult> cities;

    public WeatherHisSource(WeatherHistoryDao weatherHistoryDao){
        this.weatherHistoryDao = weatherHistoryDao;
    }

    public List<CityAndTemperatureResult> getData(){
        if (cities == null){
            loadCities();
        }
        return cities;
    }

    public void loadCities(){
        cities = weatherHistoryDao.getCitiesWithTemp();
    }

    public void addCity(CityEntity cityEntity){
        cityEntity.id = weatherHistoryDao.insertCity(cityEntity);
        loadCities();
    }

    public void addTemp(TemperatureEntity temperatureEntity){
        weatherHistoryDao.insertTemp(temperatureEntity);
        loadCities();
    }

    public void updateCityInfo(TemperatureEntity temperatureEntity){
        weatherHistoryDao.updateCityTemp(temperatureEntity);
        loadCities();
    }

    public void removeCity(long id){
        weatherHistoryDao.deleteCityById(id);
        loadCities();
    }




}
