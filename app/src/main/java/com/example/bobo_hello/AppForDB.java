package com.example.bobo_hello;

import android.app.Application;
import androidx.room.Room;
import com.example.bobo_hello.UI.SideNavigationItems.History.WeatherHistoryDB;
import com.example.bobo_hello.UI.SideNavigationItems.History.WeatherHistoryDao;

public class AppForDB extends Application {

    private static AppForDB instance;

    private WeatherHistoryDB db;

    public static AppForDB getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        db = Room.databaseBuilder(
                getApplicationContext(),
                WeatherHistoryDB.class,
                "weather_database")
                .allowMainThreadQueries()
                .build();
    }

    public WeatherHistoryDao getWeatherDao() {
        return db.getWeatherHistoryDao();
    }

}
