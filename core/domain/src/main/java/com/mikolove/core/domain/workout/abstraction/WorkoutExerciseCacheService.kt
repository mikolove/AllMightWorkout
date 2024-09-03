package com.mikolove.core.domain.workout.abstraction

interface WorkoutExerciseCacheService {

    suspend fun isExerciseInWorkout( idWorkout: String , idExercise: String ) : Int

    suspend fun addExerciseToWorkout(workoutId : String, exerciseId : String) : Long

    suspend fun removeExerciseFromWorkout(workoutId : String, exerciseId : String) : Int

}