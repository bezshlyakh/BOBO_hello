package com.example.bobo_hello.UI.SideNavigationItems.History;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {CityEntity.CITY_NAME}, unique = true)})
public class CityEntity {

        public final static String ID = "id";
        public final static String CITY_NAME = "city_name";

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = ID)
        public long id;

        @ColumnInfo(name = CITY_NAME)
        public String cityName;

}
