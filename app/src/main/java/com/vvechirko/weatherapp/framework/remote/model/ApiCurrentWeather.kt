package com.vvechirko.weatherapp.framework.remote.model

class ApiCurrentWeather(
    val id: Int,
    val name: String,
    val timezone: Int,
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long
)