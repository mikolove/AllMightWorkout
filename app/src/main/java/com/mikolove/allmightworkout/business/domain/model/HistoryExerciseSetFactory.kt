package com.mikolove.allmightworkout.business.domain.model

import com.mikolove.allmightworkout.business.domain.util.DateUtil
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList


@Singleton
class HistoryExerciseSetFactory
@Inject
constructor(private val dateUtil: DateUtil){

    fun  createHistoryExerciseSetFromExerciseSet(
        idHistoryExerciseSet : String?,
        exerciseSet : ExerciseSet,
        created_at: String?
    ) :  HistoryExerciseSet {
        return HistoryExerciseSet(
            idHistoryExerciseSet = idHistoryExerciseSet ?: UUID.randomUUID().toString(),
            reps = exerciseSet.reps,
            weight = exerciseSet.weight,
            time = exerciseSet.time,
            restTime = exerciseSet.restTime,
            created_at = created_at?: dateUtil.getCurrentTimestamp(),
            updated_at = dateUtil.getCurrentTimestamp()
        )
    }

    fun  createHistoryExerciseSet(
        idHistoryExerciseSet : String,
        reps: Int ,
        weight: Int ,
        time : Int,
        restTime : Int,
        created_at: String,
        updated_at: String
    ) :  HistoryExerciseSet {
        return HistoryExerciseSet(
            idHistoryExerciseSet = idHistoryExerciseSet ,
            reps = reps,
            weight = weight,
            time = time,
            restTime = restTime,
            created_at = created_at,
            updated_at = updated_at
        )
    }
}