package com.vvechirko.weatherapp.framework.remote.mapper

import com.vvechirko.core.domain.CityEntity
import com.vvechirko.core.domain.ForecastEntity
import com.vvechirko.core.domain.WeatherForecast
import com.vvechirko.weatherapp.framework.remote.model.ApiWeatherForecast
import java.util.*

fun ApiWeatherForecast.map(): WeatherForecast {
    val city = CityEntity(
        id = city.id,
        name = city.name
    )
    val list = list.map {
        ForecastEntity(
            date = Date(it.dt * 1000),
            main = it.weather[0].main,
            description = it.weather[0].description,
            icon = it.weather[0].iconUrl,
            clouds = it.clouds.all,
            windSpeed = it.wind.speed,
            temp = it.main.temp,
            feelsLike = it.main.feels_like,
            tempMin = it.main.temp_min,
            tempMax = it.main.temp_max,
            pressure = it.main.pressure,
            humidity = it.main.humidity
        )
    }
    return WeatherForecast(city, list)
}