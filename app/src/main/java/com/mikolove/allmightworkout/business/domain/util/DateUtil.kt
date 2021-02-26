package com.mikolove.allmightworkout.business.domain.util

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class DateUtil
constructor(
    private val dateFormatUs : SimpleDateFormat,
    private val dateFormatLocal : SimpleDateFormat){


    /*
    Firebase String - Timestamp
     */
    fun convertFirebaseTimestampToStringData(timestamp: Timestamp): String{
        return dateFormatUs.format(timestamp.toDate())
    }

    fun convertStringDateToFirebaseTimestamp(date: String): Timestamp{
        return Timestamp(dateFormatUs.parse(date))
    }

    /*
    Room String - Date
     */
    fun convertDateToStringDate(date : Date) : String{
        return dateFormatUs.format(date)
    }

    fun convertStringDateToDate(date : String) : Date{
        return dateFormatUs.parse(date)
    }

    /*
    Other
     */
    fun removeTimeFromDateString(sd: String): String{
        return sd.substring(0, sd.indexOf(" "))
    }

    fun getCurrentTimestamp(): String {
        return dateFormatUs.format(Date())
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

    /*
        Show date in current local
     */

    fun showDateInCurrentLocal(dateToShow : String) : String{
        val date = convertStringDateToDate(dateToShow)
        return dateFormatLocal.format(date)
    }
}