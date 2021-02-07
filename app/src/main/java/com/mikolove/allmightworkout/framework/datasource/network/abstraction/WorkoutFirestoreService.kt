package com.mikolove.allmightworkout.framework.datasource.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.Workout

interface WorkoutFirestoreService{

    suspend fun insertWorkout(workout: Workout)

    suspend fun updateWorkout(workout: Workout)

    suspend fun removeWorkout(id :String)

    suspend fun getExerciseIdsUpdate(idWorkout : String) : String?

    suspend fun updateExerciseIdsUpdatedAt(idWorkout: String, exerciseIdsUpdatedAt: String?)

    suspend fun getWorkouts() : List<Workout>

    suspend fun getWorkoutById(primaryKey : String) : Workout?

    suspend fun getWorkoutTotalNumber() : Int

     suspend fun getDeletedWorkouts(): List<Workout>

     suspend fun insertDeleteWorkout(workout: Workout)

     suspend fun insertDeleteWorkouts(workouts: List<Workout>)
}