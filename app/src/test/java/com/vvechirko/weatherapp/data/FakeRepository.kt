package com.vvechirko.weatherapp.data

import com.vvechirko.core.data.WeatherRepository
import com.vvechirko.core.data.WeatherRepositoryImpl
import com.vvechirko.core.util.NetworkAvailability
import kotlinx.coroutines.Dispatchers

fun fakeRepository(): WeatherRepository = WeatherRepositoryImpl(
    FakeRemoteDataSource(),
    FakeLocalDataSource(),
    networkManager(true),
    Dispatchers.Main
)

fun networkManager(networkAvailable: Boolean) = object : NetworkAvailability {
    override fun isNetworkAvailable(): Boolean = networkAvailable
}