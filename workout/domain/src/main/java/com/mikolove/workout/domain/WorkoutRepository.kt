package com.mikolove.workout.domain

import com.mikolove.core.domain.util.DataError
import com.mikolove.core.domain.util.EmptyResult
import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {

    fun getWorkouts(searchQuery : String) : Flow<List<Workout>>

    fun getWorkoutsWithExercises(workoutTypes : List<String>) : Flow<List<Workout>>

    fun getWorkoutsWithExercisesWithGroups(workoutTypes : List<String>, groups : List<String>) : Flow<List<Workout>>

    suspend fun getWorkout(workoutId : String) : Result<Workout,DataError>

    suspend fun fetchWorkouts() : EmptyResult<DataError>

    suspend fun upsertWorkout(workout: Workout) : EmptyResult<DataError>

    suspend fun deleteWorkout(workoutId : String)
}