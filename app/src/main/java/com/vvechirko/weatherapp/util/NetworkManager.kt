package com.vvechirko.weatherapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.vvechirko.core.util.NetworkAvailability

class NetworkManager(context: Context) : NetworkAvailability {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun isNetworkAvailable(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val cp = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            cp != null && cp.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    && cp.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } else {
            connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }
}