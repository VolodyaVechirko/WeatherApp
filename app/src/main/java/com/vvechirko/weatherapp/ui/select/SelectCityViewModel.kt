package com.vvechirko.weatherapp.ui.select

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.vvechirko.core.data.Result
import com.vvechirko.core.data.WeatherRepository
import com.vvechirko.core.domain.CurrentWeather
import com.vvechirko.core.domain.LocationPoint
import com.vvechirko.weatherapp.R
import com.vvechirko.weatherapp.ui.base.BaseViewModel
import com.vvechirko.weatherapp.util.CombinedLiveData
import kotlinx.coroutines.launch

class SelectCityViewModel(
    private val repository: WeatherRepository
) : BaseViewModel() {

    private val _currentCity = MutableLiveData<CurrentWeather>()
    private val savedCities = repository.citiesLiveData()

    // merge current city data and user added cities
    val citiesList: LiveData<List<CurrentWeather>> = CombinedLiveData(_currentCity, savedCities) { data1, data2 ->
        val result = data2?.toMutableList() ?: mutableListOf()
        if (data1 != null) {
            result.add(0, data1)
        }
        result
    }
    val hasCurrentLocation: LiveData<Boolean> = Transformations.map(_currentCity) { it != null }
    val citiesEmpty: LiveData<Boolean> = Transformations.map(citiesList) { it.isEmpty() }

    private var currentLocation: LocationPoint? = null

    fun refresh() {
        _dataLoading.value = true
        launch {
            val point = currentLocation
            // fetch city by current location
            if (point != null) {
                val result = repository.currentWeather(point)
                if (result is Result.Success) {
                    _currentCity.value = result.data
                } else {
                    showSnackbarMessage(R.string.loading_error)
                }
            }

            val cities = repository.queryCities()
            // fetch saved cities forecast
            if (cities is Result.Success && cities.data.isNotEmpty()) {
                val result = repository.currentWeather(cities.data)
                if (result is Result.Success) {
                    // data will be observed
                } else {
                    showSnackbarMessage(R.string.loading_error)
                }
            }

            _dataLoading.value = false
        }
    }

    fun onLocationReceived(point: LocationPoint) {
        currentLocation = point
        refresh()
    }

    fun addCity(name: String) {
        _dataLoading.value = true
        launch {
            val result = repository.currentWeather(name)
            if (result is Result.Success) {
                // data will be observed
            } else {
                showSnackbarMessage(R.string.loading_error)
            }
            _dataLoading.value = false
        }
    }
}