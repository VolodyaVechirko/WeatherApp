package com.vvechirko.core.data

import com.vvechirko.core.domain.CityEntity
import com.vvechirko.core.domain.CurrentWeather
import com.vvechirko.core.domain.WeatherForecast
import com.vvechirko.core.domain.LocationPoint
import io.reactivex.Single

interface RemoteDataSource {
    fun currentWeather(point: LocationPoint): Single<CurrentWeather>
    fun currentWeather(name: String): Single<CurrentWeather>
    fun currentWeather(group: List<CityEntity>): Single<List<CurrentWeather>>
    fun weatherForecast(cityId: Int): Single<WeatherForecast>
}