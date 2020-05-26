package com.vvechirko.core.data

import com.vvechirko.core.domain.CityEntity
import com.vvechirko.core.domain.CurrentWeather
import io.reactivex.Completable
import io.reactivex.Single

interface LocalDataSource {
    fun updateWeather(list: List<CurrentWeather>): Completable
    fun saveWeather(forecast: CurrentWeather): Completable
    fun queryCities(): Single<List<CityEntity>>
    fun removeCity(cityId: Int): Completable
    fun queryWeather(group: List<CityEntity>): Single<List<CurrentWeather>>
}