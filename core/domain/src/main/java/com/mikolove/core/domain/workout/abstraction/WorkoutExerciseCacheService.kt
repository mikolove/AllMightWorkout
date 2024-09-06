package com.mikolove.core.domain.workout.abstraction

import com.mikolove.core.domain.exercise.ExerciseSet

interface WorkoutExerciseCacheService {

    suspend fun isExerciseInWorkout( idWorkout: String , idExercise: String ) : Boolean

    suspend fun addExerciseToWorkout(workoutId : String, exerciseId : String, exerciseSet : ExerciseSet) : Long

    suspend fun removeExerciseFromWorkout(workoutId : String, exerciseId : String, sets : List<ExerciseSet>) : Int

}