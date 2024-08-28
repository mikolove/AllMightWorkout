package com.mikolove.core.domain.exercise.abstraction

import com.mikolove.core.domain.exercise.Exercise

interface ExerciseNetworkService {

    suspend fun insertExercise(exercise: Exercise)

    suspend fun updateExercise(exercise: Exercise)

    suspend fun removeExerciseById(primaryKey :String)

    suspend fun getExercises() : List<Exercise>

    suspend fun getExerciseById(primaryKey: String) : Exercise?

    suspend fun getExercisesByWorkout( idWorkout : String ) : List<Exercise>?

    suspend fun getTotalExercises() : Int

    suspend fun addExerciseToWorkout( idWorkout: String , idExercise: String)

    suspend fun removeExerciseFromWorkout( idWorkout: String , idExercise: String)

    suspend fun isExerciseInWorkout( idWorkout: String , idExercise: String ) : Int

    suspend fun getDeletedExercises() : List<Exercise>

    suspend fun insertDeletedExercise(exercise: Exercise)

    suspend fun insertDeletedExercises(exercises: List<Exercise>)
}