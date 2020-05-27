package com.vvechirko.weatherapp.ui.select

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.vvechirko.core.data.Result
import com.vvechirko.core.data.WeatherRepository
import com.vvechirko.core.domain.CurrentWeather
import com.vvechirko.core.domain.LocationPoint
import com.vvechirko.weatherapp.R
import com.vvechirko.weatherapp.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SelectCityViewModel(
    private val repository: WeatherRepository
) : BaseViewModel() {

    private val _currentCity = MutableLiveData<CurrentWeather>()
    private val _savedCities = MutableLiveData<List<CurrentWeather>>()

    // merge current city data and added cities
    private val _citiesList = MediatorLiveData<List<CurrentWeather>>().apply {
        addSource(_currentCity) { city ->
            val list = _savedCities.value?.toMutableList() ?: mutableListOf()
            list.add(0, city)
            value = list
        }

        addSource(_savedCities) { cities ->
            val list = cities.toMutableList()
            _currentCity.value?.let {
                list.add(0, it)
            }
            value = list
        }
    }

    val citiesList: LiveData<List<CurrentWeather>> = _citiesList
    val hasCurrentLocation: LiveData<Boolean> = Transformations.map(_currentCity) { it != null }
    val citiesEmpty: LiveData<Boolean> = Transformations.map(_citiesList) { it.isEmpty() }

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
                    _savedCities.value = result.data.toMutableList()
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
                val list = _savedCities.value?.toMutableList() ?: mutableListOf()
                list.add(result.data)
                _savedCities.value = list
            } else {
                showSnackbarMessage(R.string.loading_error)
            }
            _dataLoading.value = false
        }
    }
}