package com.vvechirko.weatherapp.framework.remote.mapper

import com.vvechirko.core.domain.CityEntity
import com.vvechirko.core.domain.CurrentWeather
import com.vvechirko.core.domain.ForecastEntity
import com.vvechirko.weatherapp.framework.remote.model.ApiCurrentWeather
import java.util.*

fun ApiCurrentWeather.map(): CurrentWeather {
    val city = CityEntity(
        id = id,
        name = name
    )
    val forecast = ForecastEntity(
        date = Date(dt * 1000),
        main = weather[0].main,
        description = weather[0].description,
        icon = weather[0].iconUrl,
        clouds = clouds.all,
        windSpeed = wind.speed,
        temp = main.temp,
        feelsLike = main.feels_like,
        tempMin = main.temp_min,
        tempMax = main.temp_max,
        pressure = main.pressure,
        humidity = main.humidity
    )
    return CurrentWeather(city, forecast)
}