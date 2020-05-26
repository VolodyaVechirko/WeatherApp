package com.vvechirko.weatherapp.framework.local

import androidx.room.TypeConverter
import java.util.*

class DateTypeConverter {

    @TypeConverter
    fun toDate(value: Long?) = value?.let { Date(value) }

    @TypeConverter
    fun toLong(value: Date?) = value?.time
}