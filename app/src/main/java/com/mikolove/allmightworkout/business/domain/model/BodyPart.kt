package com.mikolove.allmightworkout.business.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BodyPart(
    var idBodyPart: String,
    var name: String) : Parcelable{


    override fun toString(): String {
        return name.replaceFirstChar { it.uppercaseChar() }
    }
}