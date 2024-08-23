package com.mikolove.allmightworkout.util

import com.google.firebase.Timestamp
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

fun ZonedDateTime.toFirebaseTimestamp() : Timestamp{
    return Timestamp(this.toInstant())
}

fun Timestamp.toZoneDateTime() : ZonedDateTime{
    val timestamp = Instant.from(this.toInstant())
    return ZonedDateTime.ofInstant(timestamp, ZoneId.systemDefault())
}