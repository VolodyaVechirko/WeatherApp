package com.vvechirko.weatherapp.framework.remote.model

class ApiWeatherForecast(
    val city: City,
    val list: List<ForecastItem>
)