package com.vvechirko.weatherapp.ui.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vvechirko.core.data.WeatherRepository
import com.vvechirko.weatherapp.data.fakeRepository
import com.vvechirko.weatherapp.util.MainCoroutineRule
import com.vvechirko.weatherapp.util.testValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CityDetailsViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val repository: WeatherRepository = fakeRepository()
    private lateinit var viewModel: CityDetailsViewModel

    @Before
    fun setup() {
        viewModel = CityDetailsViewModel(1, repository)
        viewModel.refresh()
    }

    @Test
    fun `City forecast load test`() {
        assertEquals(viewModel.city.testValue().name.isNotEmpty(), true)
        assertEquals(viewModel.forecasts.testValue().size, 3)

        val firstForecast = viewModel.forecasts.value!!.first()
        assertEquals(viewModel.selectedForecast.testValue(), firstForecast)
    }

    @Test
    fun `Select forecast test`() {
        val secondForecast = viewModel.forecasts.value!![1]
        viewModel.select(secondForecast)
        assertEquals(viewModel.selectedForecast.testValue(), secondForecast)

        val thirdForecast = viewModel.forecasts.value!![2]
        viewModel.select(thirdForecast)
        assertEquals(viewModel.selectedForecast.testValue(), thirdForecast)
    }

    @Test
    fun `City deleted test`() {
        viewModel.delete()
        assertEquals(viewModel.cityDeleted.testValue().content, true)
    }
}