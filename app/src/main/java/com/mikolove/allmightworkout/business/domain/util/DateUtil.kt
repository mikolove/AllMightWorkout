package com.mikolove.allmightworkout.business.domain.util

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class DateUtil
constructor( private val dateFormat : SimpleDateFormat){


    /*
    Firebase String - Timestamp
     */
    fun convertFirebaseTimestampToStringData(timestamp: Timestamp): String{
        return dateFormat.format(timestamp.toDate())
    }

    fun convertStringDateToFirebaseTimestamp(date: String): Timestamp{
        return Timestamp(dateFormat.parse(date))
    }

    /*
    Room String - Date
     */
    fun convertDateToStringDate(date : Date) : String{
        return dateFormat.format(date)
    }

    fun convertStringDateToDate(date : String) : Date{
        return dateFormat.parse(date)
    }

    /*
    Other
     */
    fun removeTimeFromDateString(sd: String): String{
        return sd.substring(0, sd.indexOf(" "))
    }

    fun getCurrentTimestamp(): String {
        return dateFormat.format(Date())
    }

    fun getCurrentDate() : Date{
        return convertStringDateToDate(getCurrentTimestamp())
    }

    fun getCurrentDateLessMonth( month : Int) : Date{
        val calendar = Calendar.getInstance()
        calendar.time = getCurrentDate()
        calendar.add(Calendar.MONTH,-month)
        return calendar.time
    }
}