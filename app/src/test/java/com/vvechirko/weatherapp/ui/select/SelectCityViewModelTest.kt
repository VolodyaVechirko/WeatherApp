package com.vvechirko.weatherapp.ui.select

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vvechirko.core.data.WeatherRepository
import com.vvechirko.core.domain.LocationPoint
import com.vvechirko.weatherapp.data.fakeRepository
import com.vvechirko.weatherapp.util.MainCoroutineRule
import com.vvechirko.weatherapp.util.testValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SelectCityViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val repository: WeatherRepository = fakeRepository()
    private lateinit var viewModel: SelectCityViewModel

    @Before
    fun setup() {
        viewModel = SelectCityViewModel(repository)
        viewModel.refresh()
    }

    @Test
    fun `Empty cities test`() {
        // citiesList should be empty
        assertEquals(viewModel.citiesList.testValue().isEmpty(), true)
    }

    @Test
    fun `Current location city test`() {
        val point = LocationPoint(1f, 1f)
        viewModel.onLocationReceived(point)
        // citiesList should have one element
        assertEquals(viewModel.citiesList.testValue().size, 1)
        assertEquals(viewModel.hasCurrentLocation.testValue(), true)
    }

    @Test
    fun `Add city test`() {
        viewModel.addCity("London")
        // citiesList should have 1 elements
        assertEquals(viewModel.citiesList.testValue().size, 1)

        viewModel.addCity("Manchester")
        // citiesList should have 2 elements
        assertEquals(viewModel.citiesList.testValue().size, 2)
    }

    @Test
    fun `Merged cities test`() {
        val point = LocationPoint(1f, 1f)
        viewModel.onLocationReceived(point)
        // citiesList should have 1 elements - currentPoint weather
        assertEquals(viewModel.citiesList.testValue().size, 1)
        val currentPoint = viewModel.citiesList.value!!.first()
        // add several cities
        viewModel.addCity("London")
        viewModel.addCity("Manchester")
        // citiesList should have 3 elements
        assertEquals(viewModel.citiesList.testValue().size, 3)
        // citiesList should have currentPoint weather as first element
        assertEquals(viewModel.citiesList.testValue().first(), currentPoint)
    }

    @Test
    fun `Remove city test`() {
        viewModel.addCity("London")
        viewModel.addCity("Manchester")
        // citiesList should have 2 elements
        assertEquals(viewModel.citiesList.testValue().size, 2)

        val city = viewModel.citiesList.value!!.first()
        // emulate city removing
        mainCoroutineRule.runBlockingTest {
            repository.removeCity(city.city.id)
        }

        assertEquals(viewModel.citiesList.testValue().size, 1)
    }
}