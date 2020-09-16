package com.example.bobo_hello.UI.SideNavigationItems.History;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface WeatherHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertCity(CityEntity city);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTemp(TemperatureEntity temperatureEntity);

    @Update
    void updateCityTemp(TemperatureEntity temperatureEntity);

    @Delete
    void deleteCity(CityEntity city);

    @Query("DELETE FROM cityentity WHERE id = :id")
    void deleteCityById(long id);

    @Query("SELECT * FROM cityentity")
    List<CityEntity> getAllCities();

    @Query("SELECT * " +
            "FROM cityentity " +
            "INNER JOIN temperatureentity ON cityentity.id = temperatureentity.city_id")
    List<CityAndTemperatureResult> getCitiesWithTemp();

    @Query("SELECT * FROM cityentity WHERE id = :id")
    CityEntity getCityById(long id);

    @Query("SELECT * FROM cityentity WHERE city_name = :name")
    CityEntity getCityInfoByCityName(String name);

    @Query("SELECT COUNT() FROM cityentity")
    long getCountCities();

    @Query("SELECT city_name FROM cityentity")
    List<String> getAllCityNames();

}
