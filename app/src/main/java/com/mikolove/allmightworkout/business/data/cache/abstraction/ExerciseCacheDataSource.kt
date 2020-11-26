package com.mikolove.allmightworkout.business.data.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet

interface ExerciseCacheDataSource {

    suspend fun insertExercise(exercise: Exercise) : Long

    suspend fun updateExercise(primaryKey: String, exercise: Exercise) : Int

    suspend fun removeExerciseById(primaryKey :String) : Int

    suspend fun addSets(sets : List<ExerciseSet>) : LongArray

    suspend fun removeSets(sets : List<ExerciseSet>) : Int

    suspend fun getExercises(query : String, filterAndOrder : String, page : Int) : List<Exercise>

    suspend fun getExerciseById(primaryKey: String) : Exercise?

    suspend fun getTotalExercises() : Int
}