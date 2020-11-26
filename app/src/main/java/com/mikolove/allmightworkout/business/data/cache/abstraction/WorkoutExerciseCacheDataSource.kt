package com.mikolove.allmightworkout.business.data.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.Workout

interface WorkoutExerciseCacheDataSource {

    suspend fun getExercisesFromWorkoutId(workoutId: String) : List<Exercise>?

    suspend fun getWorkoutsFromExerciseId(exerciseId: String) : List<Workout>?

    suspend fun addExerciseToWorkout(workoutId : String, exerciseId : String) : Long

    suspend fun removeExerciseFromWorkout(workoutId : String, exerciseId : String) : Int

}