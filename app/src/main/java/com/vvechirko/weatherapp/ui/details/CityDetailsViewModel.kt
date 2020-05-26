package com.vvechirko.weatherapp.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vvechirko.core.data.WeatherRepository
import com.vvechirko.core.domain.CityEntity
import com.vvechirko.core.domain.ForecastEntity
import com.vvechirko.weatherapp.R
import com.vvechirko.weatherapp.ui.base.BaseViewModel
import com.vvechirko.weatherapp.ui.base.Event
import io.reactivex.android.schedulers.AndroidSchedulers

class CityDetailsViewModel(
    private val cityId: Int,
    private val repository: WeatherRepository
) : BaseViewModel() {

    private val _city = MutableLiveData<CityEntity>()
    val city: LiveData<CityEntity> = _city

    private val _forecasts = MutableLiveData<List<ForecastEntity>>()
    val forecasts: LiveData<List<ForecastEntity>> = _forecasts

    private val _selectedForecast = MutableLiveData<ForecastEntity>()
    val selectedForecast: LiveData<ForecastEntity> = _selectedForecast

    private val _cityDeleted = MutableLiveData<Event<Boolean>>()
    val cityDeleted: LiveData<Event<Boolean>> = _cityDeleted

    fun refresh() {
        disposable.add(
            repository.weatherForecast(cityId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _dataLoading.value = true }
                .doOnTerminate { _dataLoading.value = false }
                .doOnError {
                    _forecasts.value = emptyList()
                    showSnackbarMessage(R.string.loading_error)
                }
                .subscribe { it ->
                    _city.value = it.city
                    _forecasts.value = it.forecasts

                    if (it.forecasts.isNotEmpty()) {
                        select(it.forecasts.first())
                    }
                }
        )
    }

    fun select(forecast: ForecastEntity) {
        _selectedForecast.value = forecast
    }

    fun delete() {
        disposable.add(repository.removeCity(cityId)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { showSnackbarMessage(R.string.city_remove_error) }
            .subscribe { _cityDeleted.value = Event(true) }
        )
    }
}