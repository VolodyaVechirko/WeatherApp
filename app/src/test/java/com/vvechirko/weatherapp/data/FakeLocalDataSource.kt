package com.vvechirko.weatherapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.vvechirko.core.data.LocalDataSource
import com.vvechirko.core.domain.CityEntity
import com.vvechirko.core.domain.CurrentWeather

class FakeLocalDataSource : LocalDataSource {

    private val weatherData = MutableLiveData<List<CurrentWeather>>()

    override suspend fun updateWeather(list: List<CurrentWeather>) {
        weatherData.value = list
    }

    override suspend fun saveWeather(forecast: CurrentWeather) {
        val list = weatherData.value?.toMutableList() ?: mutableListOf()
        list.add(forecast)
        weatherData.value = list
    }

    override suspend fun queryCities(): List<CityEntity> {
        return queryWeather(listOf()).map { it.city }
    }

    override suspend fun removeCity(cityId: Int) {
        val list = weatherData.value?.toMutableList() ?: mutableListOf()
        list.removeAll { it.city.id == cityId }
        weatherData.value = list
    }

    override suspend fun queryWeather(group: List<CityEntity>): List<CurrentWeather> {
        return weatherData.value ?: listOf()
    }

    override fun weatherData(group: List<CityEntity>): LiveData<List<CurrentWeather>> {
        return weatherData
    }

    override fun citiesData(): LiveData<List<CityEntity>> {
        return Transformations.map(weatherData) {
            it.map { it.city }
        }
    }
}