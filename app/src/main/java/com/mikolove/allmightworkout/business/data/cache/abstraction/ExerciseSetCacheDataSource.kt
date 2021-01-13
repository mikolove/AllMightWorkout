package com.mikolove.allmightworkout.business.data.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.ExerciseSet

interface ExerciseSetCacheDataSource {

    suspend fun getExerciseSetById(primaryKey: String, idExercise: String) : ExerciseSet?

    suspend fun getExerciseSetByIdExercise(idExercise : String) : List<ExerciseSet>?

    suspend fun insertExerciseSet(exerciseSet: ExerciseSet, idExercise: String) : Long

    suspend fun updateExerciseSet(primaryKey : String, reps: Int ,weight: Int ,time : Int,restTime : Int, idExercise: String) : Int

    suspend fun removeExerciseSetById(primaryKey :String, idExercise: String) : Int

}