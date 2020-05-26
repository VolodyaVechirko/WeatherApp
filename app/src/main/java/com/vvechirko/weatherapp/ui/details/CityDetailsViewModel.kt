package com.vvechirko.weatherapp.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vvechirko.core.data.Result
import com.vvechirko.core.data.WeatherRepository
import com.vvechirko.core.domain.CityEntity
import com.vvechirko.core.domain.ForecastEntity
import com.vvechirko.weatherapp.ui.base.BaseViewModel
import com.vvechirko.weatherapp.R
import com.vvechirko.weatherapp.ui.base.Event
import kotlinx.coroutines.launch

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
        _dataLoading.value = true
        launch {
            val result = repository.weatherForecast(cityId)
            if (result is Result.Success) {
                _city.value = result.data.city
                _forecasts.value = result.data.forecasts

                if (result.data.forecasts.isNotEmpty()) {
                    select(result.data.forecasts.first())
                }
            } else {
                _forecasts.value = emptyList()
                showSnackbarMessage(R.string.loading_error)
            }

            _dataLoading.value = false
        }
    }

    fun select(forecast: ForecastEntity) {
        _selectedForecast.value = forecast
    }

    fun delete() {
        launch {
            val result = repository.removeCity(cityId)
            if (result is Result.Success) {
                _cityDeleted.value = Event(true)
            } else {
                showSnackbarMessage(R.string.city_remove_error)
            }
        }
    }
}