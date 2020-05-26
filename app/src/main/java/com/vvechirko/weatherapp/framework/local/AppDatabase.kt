package com.vvechirko.weatherapp.framework.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vvechirko.weatherapp.framework.local.dao.ForecastDao
import com.vvechirko.weatherapp.framework.local.entity.RoomCityEntity
import com.vvechirko.weatherapp.framework.local.entity.RoomForecastEntity

@Database(
    entities = [RoomCityEntity::class, RoomForecastEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DateTypeConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract val forecastDao: ForecastDao
}