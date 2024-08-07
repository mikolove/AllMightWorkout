package com.mikolove.allmightworkout.business.data.network

import com.mikolove.core.domain.workout.WorkoutNetworkDataSource
import com.mikolove.core.domain.workout.Workout

class FakeWorkoutNetworkDataSourceImpl constructor(
    private val workoutsData: HashMap<String, Workout>,
    private val deletedWorkouts : HashMap<String, Workout>
) : WorkoutNetworkDataSource {

    override suspend fun insertWorkout(workout: Workout) {
        workoutsData.put(workout.idWorkout,workout)
    }

    override suspend fun updateWorkout(workout: Workout) {
        workoutsData[workout.idWorkout] = workout
    }

    override suspend fun removeWorkout(primaryKey: String) {
        workoutsData.remove(primaryKey)
    }

    override suspend fun getExerciseIdsUpdate(idWorkout: String): String? {
        return workoutsData[idWorkout]?.exerciseIdsUpdatedAt ?:""
    }

    override suspend fun updateExerciseIdsUpdatedAt(
        idWorkout: String,
        exerciseIdsUpdatedAt: String?
    ) {
        workoutsData.get(idWorkout)?.let { workout ->
            val updatedWorkout = Workout(
                idWorkout = workout.idWorkout,
                name = workout.name,
                exercises = null,
                isActive = workout.isActive,
                startedAt = null,
                endedAt = null,
                exerciseIdsUpdatedAt = exerciseIdsUpdatedAt,
                createdAt = workoutsData.get(idWorkout)?.createdAt ?: "",
                updatedAt = workoutsData.get(idWorkout)?.updatedAt ?: ""
            )
            workoutsData.put(idWorkout, updatedWorkout)
             // success
        }
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