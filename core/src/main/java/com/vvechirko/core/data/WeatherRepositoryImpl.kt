package com.vvechirko.core.data

import androidx.lifecycle.Transformations
import com.vvechirko.core.domain.CityEntity
import com.vvechirko.core.domain.LocationPoint
import com.vvechirko.core.util.NetworkAvailability
import com.vvechirko.core.util.NoInternetException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val networkAvailability: NetworkAvailability,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : WeatherRepository {
    override suspend fun queryCities() = withContext(dispatcher) {
        try {
            Result.Success(localDataSource.queryCities())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun currentWeather(point: LocationPoint) = withContext(dispatcher) {
        try {
            if (networkAvailability.isNetworkAvailable()) {
                Result.Success(remoteDataSource.currentWeather(point))
            } else {
                // Fetch current location forecast is not supported without network
                Result.Error(NoInternetException())
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun currentWeather(name: String) = withContext(dispatcher) {
        try {
            if (networkAvailability.isNetworkAvailable()) {
                val forecast = remoteDataSource.currentWeather(name).also {
                    localDataSource.saveWeather(it)
                }
                Result.Success(forecast)
            } else {
                // Add new city forecast is not supported without network
                Result.Error(NoInternetException())
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun currentWeather(group: List<CityEntity>) = withContext(dispatcher) {
        try {
            val data = if (networkAvailability.isNetworkAvailable()) {
                remoteDataSource.currentWeather(group).also {
                    localDataSource.updateWeather(it)
                }
            } else {
                // return saved forecast without network
                localDataSource.queryWeather(group)
            }
            Result.Success(data)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun weatherForecast(cityId: Int) = withContext(dispatcher) {
        try {
            if (networkAvailability.isNetworkAvailable()) {
                Result.Success(remoteDataSource.weatherForecast(cityId))
            } else {
                // Long term forecast is not supported without network
                Result.Error(NoInternetException())
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun removeCity(cityId: Int) = withContext(dispatcher) {
        try {
            Result.Success(localDataSource.removeCity(cityId))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun citiesLiveData() = Transformations.switchMap(localDataSource.citiesData()) {
        localDataSource.weatherData(it)
    }
}