package com.mikolove.allmightworkout.business.data.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.ExerciseSet

interface ExerciseSetCacheDataSource {

    suspend fun insertExerciseSet(exerciseSet: ExerciseSet, exerciseId : String) : Long

    suspend fun updateExerciseSet(primaryKey : String, reps: Int ,weight: Int ,time : Int,restTime : Int) : Int

    suspend fun removeExerciseSetById(primaryKey :String) : Int

    suspend fun getExerciseSetsByExerciseId(idExercise: String) : List<ExerciseSet>

    suspend fun getTotalExerciseSet() : Int
}