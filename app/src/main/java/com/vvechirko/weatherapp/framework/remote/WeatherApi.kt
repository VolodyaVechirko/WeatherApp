package com.vvechirko.weatherapp.framework.remote

import com.vvechirko.weatherapp.framework.remote.model.ApiCurrentWeather
import com.vvechirko.weatherapp.framework.remote.model.ApiCurrentWeatherGroup
import com.vvechirko.weatherapp.framework.remote.model.ApiWeatherForecast
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface WeatherApi {

    //    http://api.openweathermap.org/data/2.5/forecast?q=lviv&appid=xxx
    //    http://api.openweathermap.org/data/2.5/forecast?id=988&appid=xxx
    @GET("forecast")
    suspend fun cityDetails(@QueryMap map: Map<String, String>): ApiWeatherForecast

    //    http://api.openweathermap.org/data/2.5/weather?q=lviv&appid=xxx
    //    http://api.openweathermap.org/data/2.5/weather?id=898&appid=xxx
    //    http://api.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=xxx
    @GET("weather")
    suspend fun fetchCity(@QueryMap map: Map<String, String>): ApiCurrentWeather

    @GET("group")
    suspend fun fetchCities(@QueryMap map: Map<String, String>): ApiCurrentWeatherGroup
}