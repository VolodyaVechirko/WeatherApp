package com.vvechirko.weatherapp.framework.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import com.vvechirko.core.domain.ForecastEntity
import java.util.*

@Entity(
    tableName = "forecasts", foreignKeys = [ForeignKey(
        entity = RoomCityEntity::class,
        parentColumns = ["id"],
        childColumns = ["cityId"],
        onDelete = ForeignKey.CASCADE
    )], primaryKeys = ["cityId", "date"]
)
data class RoomForecastEntity(
    val cityId: Int,
    val date: Date,
    val main: String,
    val description: String,
    val icon: String,
    val clouds: Int,
    val windSpeed: Float,
    val temp: Float,
    val feelsLike: Float,
    val tempMin: Float,
    val tempMax: Float,
    val pressure: Int,
    val humidity: Int
) {
    constructor(cityId: Int, it: ForecastEntity) : this(
        cityId,
        it.date,
        it.main,
        it.description,
        it.icon,
        it.clouds,
        it.windSpeed,
        it.temp,
        it.feelsLike,
        it.tempMin,
        it.tempMax,
        it.pressure,
        it.humidity
    )

    fun toEntity() = ForecastEntity(
        date,
        main,
        description,
        icon,
        clouds,
        windSpeed,
        temp,
        feelsLike,
        tempMin,
        tempMax,
        pressure,
        humidity
    )
}