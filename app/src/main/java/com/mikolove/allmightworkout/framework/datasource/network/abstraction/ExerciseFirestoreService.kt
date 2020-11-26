package com.mikolove.allmightworkout.framework.datasource.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet

interface ExerciseFirestoreService {

    suspend fun insertExercise(exercise: Exercise)

    suspend fun updateExercise(primaryKey: String, exercise: Exercise)

    suspend fun removeExerciseById(primaryKey :String)

    suspend fun addSets(sets : List<ExerciseSet>)

    suspend fun removeSets(sets : List<ExerciseSet>)

    suspend fun getExercises() : List<Exercise>

    suspend fun getExerciseById(primaryKey: String) : Exercise?
}