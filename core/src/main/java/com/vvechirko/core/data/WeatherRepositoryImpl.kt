package com.vvechirko.core.data

import com.vvechirko.core.domain.CityEntity
import com.vvechirko.core.domain.LocationPoint
import com.vvechirko.core.util.NetworkAvailability
import com.vvechirko.core.util.NoInternetException
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class WeatherRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val networkAvailability: NetworkAvailability,
    private val scheduler: Scheduler = Schedulers.io()
) : WeatherRepository {
    override fun queryCities() = localDataSource.queryCities()
        .subscribeOn(scheduler)

    override fun currentWeather(point: LocationPoint) = Single.defer {
        if (networkAvailability.isNetworkAvailable()) {
            remoteDataSource.currentWeather(point)
                .subscribeOn(scheduler)
        } else {
            // Fetch current location forecast is not supported without network
            Single.error(NoInternetException())
        }
    }

    override fun currentWeather(name: String) = Single.defer {
        if (networkAvailability.isNetworkAvailable()) {
            remoteDataSource.currentWeather(name)
                .subscribeOn(scheduler)
                .flatMap { localDataSource.saveWeather(it).andThen(Single.just(it)) }
        } else {
            // Add new city forecast is not supported without network
            Single.error(NoInternetException())
        }
    }

    override fun currentWeather(group: List<CityEntity>) = Single.defer {
        if (networkAvailability.isNetworkAvailable()) {
            remoteDataSource.currentWeather(group)
                .subscribeOn(scheduler)
                .flatMap { localDataSource.updateWeather(it).andThen(Single.just(it)) }
        } else {
            // return saved forecast without network
            localDataSource.queryWeather(group)
        }
    }

    override fun weatherForecast(cityId: Int) = Single.defer {
        if (networkAvailability.isNetworkAvailable()) {
            remoteDataSource.weatherForecast(cityId)
                .subscribeOn(scheduler)
        } else {
            // Long term forecast is not supported without network
            Single.error(NoInternetException())
        }
    }

    override fun removeCity(cityId: Int) = localDataSource.removeCity(cityId)
        .subscribeOn(scheduler)
}