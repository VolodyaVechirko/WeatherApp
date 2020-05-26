package com.vvechirko.weatherapp.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {

    protected val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    protected val disposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    protected fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }
}