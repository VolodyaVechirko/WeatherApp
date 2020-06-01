package com.vvechirko.weatherapp.data

import com.vvechirko.core.data.RemoteDataSource
import com.vvechirko.core.domain.*
import java.util.*

class FakeRemoteDataSource : RemoteDataSource {
    override suspend fun currentWeather(point: LocationPoint): CurrentWeather {
        return current("point")
    }

    override suspend fun currentWeather(name: String): CurrentWeather {
        return current(name)
    }

    override suspend fun currentWeather(group: List<CityEntity>): List<CurrentWeather> {
        return group.map { current(it.name) }
    }

    override suspend fun weatherForecast(cityId: Int): WeatherForecast {
        return WeatherForecast(
            mockCity("test"),
            listOf(mockForecast(), mockForecast(), mockForecast())
        )
    }

    private fun current(city: String) = CurrentWeather(
        mockCity(city), mockForecast()
    )

    private fun mockCity(name: String) = CityEntity(name.hashCode(), name)

    private fun mockForecast() = ForecastEntity(
        Date(),
        "mock",
        "mock",
        "icon",
        10,
        2f,
        18f,
        20f,
        10f,
        22f,
        100,
        80
    )
}