package com.vvechirko.weatherapp.framework.local

import com.vvechirko.core.data.LocalDataSource
import com.vvechirko.core.domain.CityEntity
import com.vvechirko.core.domain.CurrentWeather
import com.vvechirko.weatherapp.framework.local.dao.ForecastDao
import com.vvechirko.weatherapp.framework.local.entity.RoomCityEntity
import com.vvechirko.weatherapp.framework.local.entity.RoomForecastEntity
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class RoomDataSourceImpl(
    private val dao: ForecastDao
) : LocalDataSource {
    override fun updateWeather(list: List<CurrentWeather>): Completable {
        return Observable.fromIterable(list).flatMapCompletable {
            Completable.fromCallable {
                dao.update(RoomCityEntity(it.city), RoomForecastEntity(it.city.id, it.forecast))
            }
        }
    }

    override fun saveWeather(forecast: CurrentWeather): Completable {
        return Completable.fromCallable {
            dao.insert(
                RoomCityEntity(forecast.city),
                RoomForecastEntity(forecast.city.id, forecast.forecast)
            )
        }
    }

    override fun queryCities(): Single<List<CityEntity>> {
        return dao.queryCities()
            .flatMapObservable { Observable.fromIterable(it) }
            .map { it.toEntity() }
            .toList()
    }

    override fun queryWeather(group: List<CityEntity>): Single<List<CurrentWeather>> {
        return dao.queryForecasts()
            .flatMapObservable { Observable.fromIterable(it) }
            .map { CurrentWeather(it.cityEntity.toEntity(), it.forecastEntity.toEntity()) }
            .toList()
    }

    override fun removeCity(cityId: Int): Completable {
        return Completable.fromCallable {
            dao.deleteById(cityId)
        }
    }
}