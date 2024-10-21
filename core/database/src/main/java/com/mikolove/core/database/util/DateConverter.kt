package com.mikolove.core.database.util

import androidx.room.TypeConverter
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class DateConverter {

    //'2011-12-03T10:15:30+01:00[Europe/Paris]'
    private val formatter = DateTimeFormatter.ISO_DATE_TIME

    @TypeConverter
    fun fromZonedDatetime(value: ZonedDateTime?): String? {
        return formatter.format(value)
    }

    @TypeConverter
    fun dateToZonedDateTime(date: String?): ZonedDateTime? {
        return ZonedDateTime.parse(date,formatter)
    }

}