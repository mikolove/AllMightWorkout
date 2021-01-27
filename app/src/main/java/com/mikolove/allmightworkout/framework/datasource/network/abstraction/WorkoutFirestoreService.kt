package com.mikolove.allmightworkout.framework.datasource.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.Workout

interface WorkoutFirestoreService{

    suspend fun insertWorkout(workout: Workout)

    suspend fun updateWorkout(primaryKey: String, workout : Workout)

    suspend fun removeWorkout(id :String)

    suspend fun addExercises(exercises : List<Exercise>)

    suspend fun removeExercises(exercises: List<Exercise>)

    suspend fun getWorkouts() : List<Workout>

    suspend fun getWorkoutById(primaryKey : String) : Workout?

    suspend fun getWorkoutTotalNumber() : Int

     suspend fun getDeletedWorkouts(): List<Workout>

     suspend fun insertDeleteWorkout(workout: Workout)

     suspend fun insertDeleteWorkouts(workouts: List<Workout>)
}