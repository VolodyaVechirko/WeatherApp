package com.vvechirko.core.data

import com.vvechirko.core.domain.CityEntity
import com.vvechirko.core.domain.CurrentWeather
import com.vvechirko.core.domain.WeatherForecast
import com.vvechirko.core.domain.LocationPoint

interface RemoteDataSource {
    suspend fun currentWeather(point: LocationPoint): CurrentWeather
    suspend fun currentWeather(name: String): CurrentWeather
    suspend fun currentWeather(group: List<CityEntity>): List<CurrentWeather>
    suspend fun weatherForecast(cityId: Int): WeatherForecast
}