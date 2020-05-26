package com.vvechirko.core.domain

data class WeatherForecast(
    val city: CityEntity,
    val forecasts: List<ForecastEntity>
)