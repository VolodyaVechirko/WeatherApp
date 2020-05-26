package com.vvechirko.weatherapp.framework.remote

import com.vvechirko.core.data.RemoteDataSource
import com.vvechirko.core.domain.CityEntity
import com.vvechirko.core.domain.CurrentWeather
import com.vvechirko.core.domain.WeatherForecast
import com.vvechirko.core.domain.LocationPoint
import com.vvechirko.weatherapp.framework.remote.mapper.map

class RetrofitDataSourceImpl(
    private val weatherApi: WeatherApi
) : RemoteDataSource {

    private val apiKey = "cbb27ac645e4592c5a70f28532c2dac2"

    override suspend fun currentWeather(point: LocationPoint): CurrentWeather {
        val query = mapOf(
            "lat" to point.latitude.toString(),
            "lon" to point.longitude.toString(),
            "appid" to apiKey
        )
        return weatherApi.fetchCity(query).map()
    }

    override suspend fun currentWeather(name: String): CurrentWeather {
        val query = mapOf(
            "q" to name,
            "appid" to apiKey
        )
        return weatherApi.fetchCity(query).map()
    }

    override suspend fun currentWeather(group: List<CityEntity>): List<CurrentWeather> {
        val ids = group.joinToString(",") {
            it.id.toString()
        }
        val query = mapOf(
            "id" to ids,
            "appid" to apiKey
        )
        return weatherApi.fetchCities(query).list.map {
            it.map()
        }
    }

    override suspend fun weatherForecast(cityId: Int): WeatherForecast {
        val query = mapOf(
            "id" to cityId.toString(),
            "appid" to apiKey
        )
        return weatherApi.cityDetails(query).map()
    }
}