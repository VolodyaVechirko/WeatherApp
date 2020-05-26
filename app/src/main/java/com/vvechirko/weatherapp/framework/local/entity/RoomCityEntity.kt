package com.vvechirko.weatherapp.framework.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vvechirko.core.domain.CityEntity

@Entity(tableName = "cities")
data class RoomCityEntity(
    @PrimaryKey var id: Int,
    val name: String
) {
    constructor(it: CityEntity) : this(it.id, it.name)

    fun toEntity() = CityEntity(id, name)
}