package com.vvechirko.weatherapp

import android.app.Application
import com.vvechirko.weatherapp.di.apiModule
import com.vvechirko.weatherapp.di.appModule
import com.vvechirko.weatherapp.di.dbModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            logger(AndroidLogger(Level.DEBUG))
            androidContext(applicationContext)
            modules(appModule, apiModule, dbModule)
        }
    }
}