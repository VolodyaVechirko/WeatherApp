package com.vvechirko.weatherapp.framework.remote

import com.vvechirko.core.data.RemoteDataSource
import com.vvechirko.core.domain.CityEntity
import com.vvechirko.core.domain.CurrentWeather
import com.vvechirko.core.domain.LocationPoint
import com.vvechirko.core.domain.WeatherForecast
import com.vvechirko.weatherapp.framework.remote.mapper.map
import io.reactivex.Observable
import io.reactivex.Single

class RetrofitDataSourceImpl(
    private val weatherApi: WeatherApi
) : RemoteDataSource {

    private val apiKey = "cbb27ac645e4592c5a70f28532c2dac2"

    override fun currentWeather(point: LocationPoint): Single<CurrentWeather> {
        val query = mapOf(
            "lat" to point.latitude.toString(),
            "lon" to point.longitude.toString(),
            "appid" to apiKey
        )
        return weatherApi.fetchCity(query)
            .map { it.map() }
    }

    override fun currentWeather(name: String): Single<CurrentWeather> {
        val query = mapOf(
            "q" to name,
            "appid" to apiKey
        )
        return weatherApi.fetchCity(query).map { it.map() }
    }

    override fun currentWeather(group: List<CityEntity>): Single<List<CurrentWeather>> {
        val ids = group.joinToString(",") {
            it.id.toString()
        }
        val query = mapOf(
            "id" to ids,
            "appid" to apiKey
        )
        return weatherApi.fetchCities(query)
            .flatMapObservable { Observable.fromIterable(it.list) }
            .map { it.map() }
            .toList()
    }

    override fun weatherForecast(cityId: Int): Single<WeatherForecast> {
        val query = mapOf(
            "id" to cityId.toString(),
            "appid" to apiKey
        )
        return weatherApi.cityDetails(query)
            .map { it.map() }
    }
}