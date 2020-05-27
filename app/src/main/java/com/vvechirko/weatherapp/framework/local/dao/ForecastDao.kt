package com.vvechirko.weatherapp.framework.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.vvechirko.weatherapp.framework.local.entity.CityForecast
import com.vvechirko.weatherapp.framework.local.entity.RoomCityEntity
import com.vvechirko.weatherapp.framework.local.entity.RoomForecastEntity

@Dao
interface ForecastDao {
    @Query("SELECT * FROM Cities")
    suspend fun queryCities(): List<RoomCityEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCity(city: RoomCityEntity)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateCity(city: RoomCityEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertForecast(city: RoomForecastEntity)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateForecast(city: RoomForecastEntity)

    @Transaction
    suspend fun insert(city: RoomCityEntity, forecast: RoomForecastEntity) {
        insertCity(city)
        insertForecast(forecast)
    }

    @Transaction
    suspend fun update(city: RoomCityEntity, forecast: RoomForecastEntity) {
        updateCity(city)
        updateForecast(forecast)
    }

    @Query("DELETE FROM Cities WHERE id = :cityId")
    suspend fun deleteById(cityId: Int): Int

    @Transaction
    @Query("SELECT * FROM Cities")
    suspend fun queryForecasts(): List<CityForecast>


    @Query("SELECT * FROM Cities")
    fun citiesData(): LiveData<List<RoomCityEntity>>

    @Transaction
    @Query("SELECT * FROM Cities")
    fun forecastsData(): LiveData<List<CityForecast>>
}