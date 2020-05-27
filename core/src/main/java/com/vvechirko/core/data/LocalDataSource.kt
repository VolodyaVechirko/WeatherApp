package com.vvechirko.core.data

import androidx.lifecycle.LiveData
import com.vvechirko.core.domain.CityEntity
import com.vvechirko.core.domain.CurrentWeather

interface LocalDataSource {
    suspend fun updateWeather(list: List<CurrentWeather>)
    suspend fun saveWeather(forecast: CurrentWeather)
    suspend fun queryCities(): List<CityEntity>
    suspend fun removeCity(cityId: Int)
    suspend fun queryWeather(group: List<CityEntity>): List<CurrentWeather>

    fun weatherData(group: List<CityEntity>): LiveData<List<CurrentWeather>>
    fun citiesData(): LiveData<List<CityEntity>>
}