package com.example.bobo_hello.UI.SideNavigationItems.History;

import androidx.room.ColumnInfo;

public class CityAndTemperatureResult {
    @ColumnInfo(name = "city_name")
    public String cityName;

    @ColumnInfo(name = "temperature")
    public String temperature;

    @ColumnInfo(name = "date")
    public String date;

}
