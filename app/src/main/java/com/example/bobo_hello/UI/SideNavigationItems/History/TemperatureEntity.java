package com.example.bobo_hello.UI.SideNavigationItems.History;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.util.Date;
import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = CityEntity.class,
        parentColumns = "id",
        childColumns = "city_id", onDelete = CASCADE))
public class TemperatureEntity {

        @PrimaryKey(autoGenerate = true)
        public long id;

        @ColumnInfo(name = "city_id")
        public long cityId;

        public String temperature;

        public String date;
}
