package com.vvechirko.core.domain

import java.util.*

data class LocationPoint(
    val latitude: Float,
    val longitude: Float
) {
    override fun toString(): String {
        return String.format(Locale.US, "%.8f,%.8f", latitude, longitude)
    }
}