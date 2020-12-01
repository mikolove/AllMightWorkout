package com.mikolove.allmightworkout.business.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class HistoryExercise(
    var idHistoryExercise : String,
    var name : String,
    var bodyPart : String,
    var workoutType : String,
    var exerciseType: ExerciseType,
    var historySets: List<HistoryExerciseSet>,
    var created_at: String,
    var updated_at: String
) : Parcelable