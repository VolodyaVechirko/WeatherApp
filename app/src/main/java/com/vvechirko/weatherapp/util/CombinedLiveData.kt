package com.vvechirko.weatherapp.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T, K, S> CombinedLiveData(
    source1: LiveData<T>, source2: LiveData<K>, combine: (data1: T?, data2: K?) -> S
): LiveData<S> = object : MediatorLiveData<S>() {
    private var data1: T? = null
    private var data2: K? = null

    init {
        addSource(source1) {
            data1 = it
            value = combine(data1, data2)
        }
        addSource(source2) {
            data2 = it
            value = combine(data1, data2)
        }
    }
}

fun <T, K, R> LiveData<T>.combine(liveData: LiveData<K>, block: (T?, K?) -> R): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) {
        result.value = block.invoke(this.value, liveData.value)
    }
    result.addSource(liveData) {
        result.value = block.invoke(this.value, liveData.value)
    }
    return result
}