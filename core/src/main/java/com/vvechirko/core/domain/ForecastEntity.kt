package com.vvechirko.core.domain

import com.vvechirko.core.util.toCelsius
import java.util.*

data class ForecastEntity(
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
    val tempMinMax: String
        get() = "${tempMin.toCelsius()} / ${tempMax.toCelsius()}"
}