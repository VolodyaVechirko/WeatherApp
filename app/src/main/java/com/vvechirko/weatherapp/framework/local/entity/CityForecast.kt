package com.vvechirko.weatherapp.framework.local.entity

import androidx.room.Embedded
import androidx.room.Relation

class CityForecast(
    @Embedded
    val cityEntity: RoomCityEntity,

    @Relation(entity = RoomForecastEntity::class, parentColumn = "id", entityColumn = "cityId")
    val forecastEntity: RoomForecastEntity
)