package com.vvechirko.weatherapp.framework.remote.model

class ForecastItem(
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long
)