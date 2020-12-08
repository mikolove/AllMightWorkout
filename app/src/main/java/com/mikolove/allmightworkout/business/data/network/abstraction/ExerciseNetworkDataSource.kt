package com.mikolove.allmightworkout.business.data.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet

interface ExerciseNetworkDataSource {

    suspend fun insertExercise(exercise: Exercise)

    suspend fun updateExercise(primaryKey: String, exercise: Exercise)

    suspend fun removeExerciseById(primaryKey :String)

    suspend fun getExercises() : List<Exercise>

    suspend fun getTotalExercises() : Int

    suspend fun getExerciseById(primaryKey: String) : Exercise?

}