package com.vvechirko.core.data

import com.vvechirko.core.domain.CityEntity
import com.vvechirko.core.domain.CurrentWeather
import com.vvechirko.core.domain.LocationPoint
import com.vvechirko.core.domain.WeatherForecast
import io.reactivex.Completable
import io.reactivex.Single

interface WeatherRepository {
    fun queryCities(): Single<List<CityEntity>>
    fun currentWeather(point: LocationPoint): Single<CurrentWeather>
    fun currentWeather(name: String): Single<CurrentWeather>
    fun currentWeather(group: List<CityEntity>): Single<List<CurrentWeather>>
    fun weatherForecast(cityId: Int): Single<WeatherForecast>
    fun removeCity(cityId: Int): Completable
}