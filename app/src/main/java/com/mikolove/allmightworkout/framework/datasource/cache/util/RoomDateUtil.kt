package com.mikolove.allmightworkout.framework.datasource.cache.util

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


class RoomDateUtil
constructor( private val dateFormat : SimpleDateFormat) {

    fun convertDateToStringDate(date : Date) : String{
        return dateFormat.format(date)
    }

    fun convertStringDateToDate(date : String) : Date{
        return dateFormat.parse(date)
    }
}