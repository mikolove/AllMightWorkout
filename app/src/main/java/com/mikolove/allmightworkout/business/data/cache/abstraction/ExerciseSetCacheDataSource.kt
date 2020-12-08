package com.mikolove.allmightworkout.business.data.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.ExerciseSet

interface ExerciseSetCacheDataSource {

    suspend fun insertExerciseSet(exerciseSet: ExerciseSet) : Long

    suspend fun updateExerciseSet(primaryKey : String, reps: Int ,weight: Int ,time : Int,restTime : Int) : Int

    suspend fun removeExerciseSetById(primaryKey :String) : Int

}