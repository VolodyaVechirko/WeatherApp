package com.vvechirko.core.domain

data class CurrentWeather(
    val city: CityEntity,
    val forecast: ForecastEntity
)