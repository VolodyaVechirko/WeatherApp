package com.vvechirko.weatherapp.util

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<MutableList<T>>.addItem(item: T) {
    val newValue = value ?: mutableListOf()
    newValue.add(item)
    value = newValue
}