package com.mikolove.allmightworkout.business.data.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.ExerciseSet

interface ExerciseSetCacheDataSource {

    suspend fun insertExerciseSet(exerciseSet: ExerciseSet) : Long

    suspend fun updateExerciseSet(primaryKey : String, exerciseSet: ExerciseSet) : Int

    suspend fun removeExerciseSetById(primaryKey :String) : Int

    suspend fun getExerciseSetsByExerciseId(idExercise: String) : List<ExerciseSet>

    suspend fun getTotalExerciseSet() : Int
}