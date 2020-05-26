package com.vvechirko.weatherapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vvechirko.weatherapp.ui.base.BaseFragment
import com.vvechirko.weatherapp.ui.select.SelectCityFragment

class MainActivity : AppCompatActivity(), BaseFragment.Interaction {

    private val containerId = android.R.id.content

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (supportFragmentManager.findFragmentById(containerId) == null) {
            supportFragmentManager.beginTransaction()
                .replace(containerId, SelectCityFragment())
                .commit()
        }
    }

    override fun navigate(fragment: BaseFragment) {
        supportFragmentManager.beginTransaction()
            .replace(containerId, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun back() {
        onBackPressed()
    }
}
