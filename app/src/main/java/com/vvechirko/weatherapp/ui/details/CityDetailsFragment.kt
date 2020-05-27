package com.vvechirko.weatherapp.ui.details

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import coil.api.load
import com.vvechirko.core.domain.ForecastEntity
import com.vvechirko.core.util.toCelsius
import com.vvechirko.core.util.toMmHg
import com.vvechirko.core.util.toPercent
import com.vvechirko.core.util.toSpeed
import com.vvechirko.weatherapp.R
import com.vvechirko.weatherapp.ui.base.BaseFragment
import com.vvechirko.weatherapp.ui.base.EventObserver
import kotlinx.android.synthetic.main.fragment_city_details.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CityDetailsFragment : BaseFragment(), ForecastAdapter.ItemClickListener {

    companion object {
        private const val ARG_CITY_ID = "ARG_CITY_ID"

        fun newInstance(cityId: Int) = CityDetailsFragment().apply {
            arguments = Bundle().also {
                it.putInt(ARG_CITY_ID, cityId)
            }
        }
    }

    private val viewModel: CityDetailsViewModel by viewModel {
        parametersOf(arguments!!.getInt(ARG_CITY_ID))
    }
    private val adapter = ForecastAdapter(this)

    override val layoutResId: Int
        get() = R.layout.fragment_city_details

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as? AppCompatActivity)?.let {
            it.title = null
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        forecastList.adapter = adapter
        refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.dataLoading.observe(this, Observer {
            refreshLayout.isRefreshing = it
        })

        viewModel.snackbarText.observe(this, EventObserver {
            showSnackbar(it)
        })

        viewModel.selectedForecast.observe(this, Observer {
            setForecast(it)
        })

        viewModel.city.observe(this, Observer {
            activity?.title = it.name
        })

        viewModel.forecasts.observe(this, Observer {
            adapter.items = it
        })

        viewModel.cityDeleted.observe(this, EventObserver {
            back()
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.refresh()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.city_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> back()
            R.id.menu_delete -> viewModel.delete()
        }
        return super.onOptionsItemSelected(item)
    }

    // ForecastAdapter.ItemClickListener
    override fun onItemClicked(item: ForecastEntity) {
        viewModel.select(item)
    }

    private fun setForecast(it: ForecastEntity) {
        icon.load(it.icon)
        main.text = it.main
        degree.text = it.temp.toCelsius()
        feelsLike.value = it.feelsLike.toCelsius()
        minTemp.value = it.tempMin.toCelsius()
        maxTemp.value = it.tempMax.toCelsius()
        wind.value = it.windSpeed.toSpeed()
        humidity.value = it.humidity.toPercent()
        pressure.value = it.pressure.toMmHg()
    }
}