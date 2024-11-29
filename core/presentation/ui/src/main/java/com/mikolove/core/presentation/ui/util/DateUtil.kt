package com.mikolove.core.presentation.ui.util

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

//Ui formatting
fun ZonedDateTime.toUi() : String{
    return this.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
}

