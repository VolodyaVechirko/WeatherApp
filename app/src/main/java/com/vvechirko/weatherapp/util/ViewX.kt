package com.vvechirko.weatherapp.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes

inline fun ViewGroup.inflate(@LayoutRes resId: Int, attachToRoot: Boolean = false) =
    LayoutInflater.from(context).inflate(resId, this, false)