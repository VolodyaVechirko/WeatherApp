package com.vvechirko.weatherapp.di

import androidx.room.Room
import com.google.gson.Gson
import com.vvechirko.core.data.WeatherRepository
import com.vvechirko.core.data.WeatherRepositoryImpl
import com.vvechirko.core.util.NetworkAvailability
import com.vvechirko.weatherapp.BuildConfig
import com.vvechirko.weatherapp.framework.local.AppDatabase
import com.vvechirko.weatherapp.framework.local.RoomDataSourceImpl
import com.vvechirko.weatherapp.framework.remote.RetrofitDataSourceImpl
import com.vvechirko.weatherapp.ui.details.CityDetailsViewModel
import com.vvechirko.weatherapp.ui.select.SelectCityViewModel
import com.vvechirko.weatherapp.util.NetworkManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

const val WEATHER = "WEATHER"
const val API_URL = "https://api.openweathermap.org/data/2.5/"

val appModule = module {
    single<NetworkAvailability> { NetworkManager(get()) }

    single<WeatherRepository> {
        val remote = RetrofitDataSourceImpl(get<Retrofit>(named(WEATHER)).create())
        val local = RoomDataSourceImpl(get<AppDatabase>().forecastDao)
        WeatherRepositoryImpl(remote, local, get())
    }

    viewModel { SelectCityViewModel(get()) }
    viewModel { (cityId: Int) ->
        CityDetailsViewModel(cityId, get())
    }
}

val dbModule = module {
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "Weather.db")
            .build()
    }
}

val apiModule = module {
    factory {
        OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().also {
                    it.level = HttpLoggingInterceptor.Level.BODY
                })
            }
        }.build()
    }

    factory {
        Gson()
    }

    factory(named(WEATHER)) {
        Retrofit.Builder()
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .baseUrl(API_URL)
            .build()
    }
}