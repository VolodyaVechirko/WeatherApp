package com.vvechirko.weatherapp.framework.local

import androidx.lifecycle.Transformations
import com.vvechirko.core.data.LocalDataSource
import com.vvechirko.core.domain.CityEntity
import com.vvechirko.core.domain.CurrentWeather
import com.vvechirko.weatherapp.framework.local.dao.ForecastDao
import com.vvechirko.weatherapp.framework.local.entity.RoomCityEntity
import com.vvechirko.weatherapp.framework.local.entity.RoomForecastEntity

class RoomDataSourceImpl(
    private val dao: ForecastDao
) : LocalDataSource {
    override suspend fun updateWeather(list: List<CurrentWeather>) {
        list.forEach {
            dao.update(
                RoomCityEntity(it.city),
                RoomForecastEntity(it.city.id, it.forecast)
            )
        }
    }

    override suspend fun saveWeather(forecast: CurrentWeather) {
        dao.insert(
            RoomCityEntity(forecast.city),
            RoomForecastEntity(forecast.city.id, forecast.forecast)
        )
    }

    override suspend fun queryCities(): List<CityEntity> {
        return dao.queryCities().map { it.toEntity() }
    }

    override suspend fun queryWeather(group: List<CityEntity>): List<CurrentWeather> {
        return dao.queryForecasts().map {
            CurrentWeather(it.cityEntity.toEntity(), it.forecastEntity.toEntity())
        }
    }

    override suspend fun removeCity(cityId: Int) {
        dao.deleteById(cityId)
    }

    override fun citiesData() = Transformations.map(dao.citiesData()) { data ->
        data.map { it.toEntity() }
    }

    override fun weatherData(group: List<CityEntity>) = Transformations.map(dao.forecastsData()) { data ->
        data.map {
            CurrentWeather(it.cityEntity.toEntity(), it.forecastEntity.toEntity())
        }
    }
}