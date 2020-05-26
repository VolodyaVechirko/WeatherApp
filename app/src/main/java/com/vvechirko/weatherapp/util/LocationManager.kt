package com.vvechirko.weatherapp.util

import android.Manifest
import android.app.Activity
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.vvechirko.core.domain.LocationPoint
import java.lang.ref.WeakReference

class LocationManager {

    private val permission = Manifest.permission.ACCESS_FINE_LOCATION
    private var locationClient: FusedLocationProviderClient? = null
    private var weakCallback: WeakReference<Callback>? = null

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            onLocationResult(result?.lastLocation)
        }
    }

    enum class Warning {
        LOCATION_PERMISSION_DENIED,
        LOCATION_SHOW_RATIONALE,
        LOCATION_SETTINGS_DENIED
    }

    interface Callback {
        val fragment: Fragment
        fun onLocationReceived(point: LocationPoint)
        fun onDisplayWarning(w: Warning)
    }

    // should be called in onStart()
    fun tryRequestLocation(callback: Callback) {
        weakCallback = WeakReference(callback)
        weakCallback?.get()?.let {
            checkPermission(it.fragment)
        }
    }

    // should be called in onStop()
    fun cancelRequest() {
        locationClient?.removeLocationUpdates(locationCallback)
        locationClient = null
        weakCallback = null
    }

    // Location settings popup result
    fun onActivityResult(requestCode: Int, resultCode: Int) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            // Location settings allowed
            weakCallback?.get()?.let {
                if (isPermissionGranted(it.fragment)) {
                    requestLocation(it.fragment)
                }
            }
        } else {
            // Location settings denied
            weakCallback?.get()?.onDisplayWarning(Warning.LOCATION_SETTINGS_DENIED)
        }
    }

    // request Permissions result
    fun onRequestPermissionsResult(code: Int) {
        if (code == 1) {
            // permission was granted
            weakCallback?.get()?.let {
                if (isPermissionGranted(it.fragment)) {
                    requestLocation(it.fragment)
                }
            }
        } else {
            // permission denied
            weakCallback?.get()?.onDisplayWarning(Warning.LOCATION_PERMISSION_DENIED)
        }
    }

    private fun onLocationResult(location: Location?) {
        if (location != null) {
            val point = LocationPoint(location.latitude.toFloat(), location.longitude.toFloat())
            weakCallback?.get()?.onLocationReceived(point)
        }
    }

    private fun checkPermission(fragment: Fragment) {
        if (isPermissionGranted(fragment)) {
            // Permission has already been granted
            requestLocation(fragment)
        } else {
            // Permission is not granted
            // Should we show an explanation?
            if (fragment.shouldShowRequestPermissionRationale(permission)) {
                // Show an explanation to the user
                weakCallback?.get()?.onDisplayWarning(Warning.LOCATION_SHOW_RATIONALE)
            } else {
                // No explanation needed; request the permission
                fragment.requestPermissions(arrayOf(permission), 1)
            }
        }
    }

    private fun requestLocation(fragment: Fragment) {
        val locationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 20
            fastestInterval = 10
            numUpdates = 1
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val task = LocationServices.getSettingsClient(fragment.requireContext())
            .checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            locationClient = LocationServices.getFusedLocationProviderClient(fragment.requireContext())

            // request last location first
            locationClient?.lastLocation?.addOnSuccessListener { location ->
                onLocationResult(location)
            }

            // request new location update for precision
            locationClient?.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            )
        }

        task.addOnFailureListener {
            if (it is ResolvableApiException) {
                // Location settings are not satisfied, try show dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    it.startResolutionForResult(fragment, 1)
                } catch (e: IntentSender.SendIntentException) {
                    // Ignored
                }
            }
        }
    }

    @Throws(IntentSender.SendIntentException::class)
    private fun ResolvableApiException.startResolutionForResult(fragment: Fragment, code: Int) {
        if (resolution != null) {
            fragment.startIntentSenderForResult(resolution.intentSender, code, null, 0, 0, 0, null)
        }
    }

    private fun isPermissionGranted(fragment: Fragment): Boolean {
        return checkSelfPermission(fragment.requireContext(), permission) == PackageManager.PERMISSION_GRANTED
    }
}