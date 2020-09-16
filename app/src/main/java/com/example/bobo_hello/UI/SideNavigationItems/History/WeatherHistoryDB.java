package com.example.bobo_hello.UI.SideNavigationItems.History;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CityEntity.class, TemperatureEntity.class}, version = 1)
public abstract class WeatherHistoryDB extends RoomDatabase {

    public abstract WeatherHistoryDao getWeatherHistoryDao();
}
