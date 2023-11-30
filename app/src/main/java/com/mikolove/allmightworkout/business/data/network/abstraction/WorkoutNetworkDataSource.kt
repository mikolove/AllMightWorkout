package com.mikolove.allmightworkout.business.data.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.Workout

/*
    Not including User id here might be a problem to clean architecture fundamentals.
    Using firebase current User id to do all the action is ok but it should be a parameter, if we want to
    change firebase as distant DB
 */
interface WorkoutNetworkDataSource {

    suspend fun insertWorkout(workout: Workout)

    suspend fun updateWorkout(workout: Workout)

    suspend fun removeWorkout(primaryKey :String)

    suspend fun getExerciseIdsUpdate(idWorkout : String) : String?

    suspend fun updateExerciseIdsUpdatedAt(idWorkout: String, exerciseIdsUpdatedAt: String?)

    suspend fun getWorkouts() : List<Workout>

    suspend fun getWorkoutById(primaryKey : String) : Workout?

    suspend fun getWorkoutTotalNumber() : Int

    suspend fun insertDeleteWorkout(workout: Workout)

    suspend fun insertDeleteWorkouts(workouts: List<Workout>)

    suspend fun getDeletedWorkouts() : List<Workout>

}