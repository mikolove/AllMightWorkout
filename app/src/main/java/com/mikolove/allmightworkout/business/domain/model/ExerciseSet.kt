package com.mikolove.allmightworkout.business.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class ExerciseSet(
    var idExerciseSet: String,
    var reps: Int ,
    var weight: Int ,
    var time : Int,
    var restTime : Int,
    var started_at: String?,
    var ended_at: String?,
    var created_at : String,
    var updated_at : String,
) : Parcelable{

    fun start(date : String){
        started_at = date
    }

    fun stop(date : String){
        ended_at = date
    }
}