package com.mikolove.allmightworkout.business.data.network

import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.Workout

class FakeWorkoutNetworkDataSourceImpl constructor(
    private val workoutsData: HashMap<String, Workout>,
    private val deletedWorkouts : HashMap<String,Workout>
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

    override suspend fun getWorkouts(): List<Workout> {
      return ArrayList<Workout>(workoutsData.values)
    }

    override suspend fun getWorkoutById(primaryKey: String): Workout? {
        return workoutsData[primaryKey]
    }

    override suspend fun getWorkoutTotalNumber(): Int {
        return workoutsData.size
    }

    override suspend fun getDeletedWorkouts(): List<Workout> {
        return ArrayList(deletedWorkouts.values)
    }

    override suspend fun insertDeleteWorkout(workout: Workout) {
       deletedWorkouts.put(workout.idWorkout,workout)
    }

    override suspend fun insertDeleteWorkouts(workouts: List<Workout>) {
        workouts.forEach { workout ->
            deletedWorkouts.put(workout.idWorkout,workout)
        }
    }
}