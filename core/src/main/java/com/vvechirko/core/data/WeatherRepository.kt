package com.vvechirko.core.data

import androidx.lifecycle.LiveData
import com.vvechirko.core.domain.CityEntity
import com.vvechirko.core.domain.CurrentWeather
import com.vvechirko.core.domain.LocationPoint
import com.vvechirko.core.domain.WeatherForecast

interface WeatherRepository {
    suspend fun queryCities(): Result<List<CityEntity>>
    suspend fun currentWeather(point: LocationPoint): Result<CurrentWeather>
    suspend fun currentWeather(name: String): Result<CurrentWeather>
    suspend fun currentWeather(group: List<CityEntity>): Result<List<CurrentWeather>>
    suspend fun weatherForecast(cityId: Int): Result<WeatherForecast>
    suspend fun removeCity(cityId: Int): Result<Unit>

    fun citiesLiveData(): LiveData<List<CurrentWeather>>
}