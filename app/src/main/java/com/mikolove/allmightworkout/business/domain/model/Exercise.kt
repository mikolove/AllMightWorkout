package com.mikolove.allmightworkout.business.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class Exercise(
    var idExercise: String,
    var name: String,
    var sets: List<ExerciseSet>,
    var bodyPart: BodyPart?,
    var exerciseType : ExerciseType,
    var isActive: Boolean,
    var started_at: String?,
    var ended_at: String?,
    var created_at: String,
    var updated_at: String) : Parcelable{

    fun start(date : String){
        started_at = date
    }

    fun stop(date : String){
        ended_at = date
    }
}

