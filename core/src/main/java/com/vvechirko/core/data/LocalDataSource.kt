package com.vvechirko.core.data

import com.vvechirko.core.domain.CityEntity
import com.vvechirko.core.domain.CurrentWeather

interface LocalDataSource {
    suspend fun updateWeather(list: List<CurrentWeather>)
    suspend fun saveWeather(forecast: CurrentWeather)
    suspend fun queryCities(): List<CityEntity>
    suspend fun removeCity(cityId: Int)
    suspend fun queryWeather(group: List<CityEntity>): List<CurrentWeather>
}