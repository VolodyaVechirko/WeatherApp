package com.vvechirko.weatherapp.framework.remote

import com.vvechirko.weatherapp.framework.remote.model.ApiCurrentWeather
import com.vvechirko.weatherapp.framework.remote.model.ApiCurrentWeatherGroup
import com.vvechirko.weatherapp.framework.remote.model.ApiWeatherForecast
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface WeatherApi {

    //    http://api.openweathermap.org/data/2.5/forecast?q=lviv&appid=xxx
    //    http://api.openweathermap.org/data/2.5/forecast?id=988&appid=xxx
    @GET("forecast")
    fun cityDetails(@QueryMap map: Map<String, String>): Single<ApiWeatherForecast>

    //    http://api.openweathermap.org/data/2.5/weather?q=lviv&appid=xxx
    //    http://api.openweathermap.org/data/2.5/weather?id=898&appid=xxx
    //    http://api.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=xxx
    @GET("weather")
    fun fetchCity(@QueryMap map: Map<String, String>): Single<ApiCurrentWeather>

    //    http://api.openweathermap.org/data/2.5/group?id=524901,703448,2643743&units=metric
    @GET("group")
    fun fetchCities(@QueryMap map: Map<String, String>): Single<ApiCurrentWeatherGroup>
}