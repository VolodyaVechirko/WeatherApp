package com.vvechirko.weatherapp.framework.local.dao

import androidx.room.*
import com.vvechirko.weatherapp.framework.local.entity.CityForecast
import com.vvechirko.weatherapp.framework.local.entity.RoomCityEntity
import com.vvechirko.weatherapp.framework.local.entity.RoomForecastEntity
import io.reactivex.Single

@Dao
interface ForecastDao {
    @Query("SELECT * FROM Cities")
    fun queryCities(): Single<List<RoomCityEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCity(city: RoomCityEntity)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateCity(city: RoomCityEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertForecast(city: RoomForecastEntity)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateForecast(city: RoomForecastEntity)

    @Transaction
    fun insert(city: RoomCityEntity, forecast: RoomForecastEntity) {
        insertCity(city)
        insertForecast(forecast)
    }

    @Transaction
    fun update(city: RoomCityEntity, forecast: RoomForecastEntity) {
        updateCity(city)
        updateForecast(forecast)
    }

    @Query("DELETE FROM Cities WHERE id = :cityId")
    fun deleteById(cityId: Int): Int

    @Transaction
    @Query("SELECT * FROM Cities")
    fun queryForecasts(): Single<List<CityForecast>>
}