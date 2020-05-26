package com.vvechirko.weatherapp.ui.select

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.vvechirko.core.data.WeatherRepository
import com.vvechirko.core.domain.CurrentWeather
import com.vvechirko.core.domain.LocationPoint
import com.vvechirko.weatherapp.R
import com.vvechirko.weatherapp.ui.base.BaseViewModel
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers

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
    val empty: LiveData<Boolean> = Transformations.map(_citiesList) {
        it.isEmpty()
    }

    private var currentLocation: LocationPoint? = null

    fun refresh() {
        val point = currentLocation
        disposable.add(
            Maybe.merge(
                Maybe.defer {
                    if (point != null) {
                        // fetch city by current location
                        repository.currentWeather(point)
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnError { showSnackbarMessage(R.string.loading_error) }
                            .doOnSuccess { _currentCity.value = it }
                            .toMaybe()
                    } else {
                        Maybe.empty()
                    }
                },

                // fetch saved cities forecast
                repository.queryCities()
                    .filter { it.isNotEmpty() }
                    .flatMapSingle { repository.currentWeather(it) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError { showSnackbarMessage(R.string.loading_error) }
                    .doOnSuccess { _savedCities.value = it.toMutableList() }
                    .toMaybe()

            ).doOnSubscribe { _dataLoading.value = true }
                .doOnComplete { _dataLoading.value = false }
                .subscribe()
        )
    }

    fun onLocationReceived(point: LocationPoint) {
        currentLocation = point
        refresh()
    }

    fun addCity(name: String) {
        disposable.add(repository.currentWeather(name)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _dataLoading.value = true }
            .doOnTerminate { _dataLoading.value = false }
            .doOnError { showSnackbarMessage(R.string.loading_error) }
            .subscribe { it ->
                val list = _savedCities.value?.toMutableList() ?: mutableListOf()
                list.add(it)
                _savedCities.value = list
            }
        )
    }
}