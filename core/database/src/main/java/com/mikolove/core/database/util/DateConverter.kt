package com.mikolove.core.database.util

import androidx.room.TypeConverter
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class DateConverter {

    @TypeConverter
    fun fromZdtime(value: ZonedDateTime?): Date? {
        return value?.let { Date.from(it.toInstant()) }
    }

    @TypeConverter
    fun dateToZdt(date: Date?): ZonedDateTime? {
        return ZonedDateTime.ofInstant(
            date?.toInstant(),
            ZoneId.systemDefault()
        )
    }

}