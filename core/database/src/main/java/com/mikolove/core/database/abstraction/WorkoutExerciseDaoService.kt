package com.mikolove.core.database.abstraction

interface WorkoutExerciseDaoService {

    //suspend fun getExercisesFromWorkoutId(workoutId: String) : List<Exercise>?

    //suspend fun getWorkoutsFromExerciseId(exerciseId: String) : List<Workout>?

    suspend fun isExerciseInWorkout( idWorkout: String , idExercise: String ) : Int

    suspend fun addExerciseToWorkout(workoutId : String, exerciseId : String) : Long

    suspend fun removeExerciseFromWorkout(workoutId : String, exerciseId : String) : Int

}