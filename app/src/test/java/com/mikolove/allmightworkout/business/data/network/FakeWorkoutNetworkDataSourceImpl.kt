package com.mikolove.allmightworkout.business.data.network

import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.Workout

class FakeWorkoutNetworkDataSourceImpl constructor(
    private val workoutsData: HashMap<String, Workout>
) : WorkoutNetworkDataSource{

    override suspend fun insertWorkout(workout: Workout) {
        workoutsData.put(workout.idWorkout,workout)
    }

    override suspend fun updateWorkout(primaryKey: String,workout: Workout) {
        workoutsData[primaryKey] = workout
    }

    override suspend fun removeWorkout(primaryKey: String) {
        workoutsData.remove(primaryKey)
    }

    override suspend fun addExercises(exercises: List<Exercise>) {
        TODO("Not yet implemented")
    }

    override suspend fun removeExercises(exercises: List<Exercise>) {
        TODO("Not yet implemented")
    }

    override suspend fun getWorkout(): List<Workout> {
      return ArrayList<Workout>(workoutsData.values)
    }

    override suspend fun getWorkoutById(primaryKey: String): Workout? {
        return workoutsData[primaryKey]
    }

    override suspend fun getWorkoutTotalNumber(): Int {
        return workoutsData.size
    }

}