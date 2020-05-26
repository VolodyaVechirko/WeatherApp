package com.vvechirko.weatherapp.ui.select

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.vvechirko.core.domain.CityEntity
import com.vvechirko.core.domain.LocationPoint
import com.vvechirko.weatherapp.R
import com.vvechirko.weatherapp.ui.base.BaseFragment
import com.vvechirko.weatherapp.ui.base.EventObserver
import com.vvechirko.weatherapp.ui.details.CityDetailsFragment
import com.vvechirko.weatherapp.util.LocationManager
import kotlinx.android.synthetic.main.fragment_select_city.*
import org.koin.android.viewmodel.ext.android.viewModel

class SelectCityFragment : BaseFragment(), LocationManager.Callback,
    CitiesAdapter.ItemClickListener {

    private val viewModel: SelectCityViewModel by viewModel()
    private val locationManager = LocationManager()
    private val adapter = CitiesAdapter(this)

    override val layoutResId: Int
        get() = R.layout.fragment_select_city

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        citiesList.adapter = adapter
        refreshLayout.setOnRefreshListener(viewModel::refresh)
        fabAdd.setOnClickListener(::showAddCityDialog)

        viewModel.citiesList.observe(this, Observer {
            adapter.items = it
        })

        viewModel.dataLoading.observe(this, Observer {
            refreshLayout.isRefreshing = it
        })

        viewModel.snackbarText.observe(this, EventObserver {
            showSnackbar(it)
        })
    }

    override fun onStart() {
        super.onStart()
        (activity as? AppCompatActivity)?.let {
            it.setTitle(R.string.app_name)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        viewModel.refresh()
        locationManager.tryRequestLocation(this)
    }

    override fun onStop() {
        super.onStop()
        locationManager.cancelRequest()
    }

    // CitiesAdapter.ItemClickListener
    override fun onItemClicked(item: CityEntity) {
        navigate(CityDetailsFragment.newInstance(item.id))
    }

    override val fragment: Fragment
        get() = this

    override fun onLocationReceived(point: LocationPoint) {
        viewModel.onLocationReceived(point)
    }

    override fun onDisplayWarning(w: LocationManager.Warning) {
        // Ignored
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        locationManager.onActivityResult(requestCode, resultCode)
    }

    override fun onRequestPermissionsResult(code: Int, p: Array<String>, res: IntArray) {
        locationManager.onRequestPermissionsResult(code)
    }

    private fun showAddCityDialog(v: View) {
        AddCityDialog(v.context, onDone = { input ->
            viewModel.addCity(input)
        }).show()
    }
}