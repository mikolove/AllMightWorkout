package com.mikolove.allmightworkout.business.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class Exercise(
    var idExercise: String,
    var name: String,
    var sets: List<ExerciseSet>,
    var bodyPart: BodyPart,
    var exerciseType : ExerciseType,
    var isActive: Boolean,
    var created_at: String,
    var updated_at: String) : Parcelable

