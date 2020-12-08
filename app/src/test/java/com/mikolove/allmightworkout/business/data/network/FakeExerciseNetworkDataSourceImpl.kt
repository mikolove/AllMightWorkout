package com.mikolove.allmightworkout.business.data.network

import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.Exercise

class FakeExerciseNetworkDataSourceImpl(
    private val exercisesData : HashMap<String, Exercise>
) : ExerciseNetworkDataSource {

    override suspend fun insertExercise(exercise: Exercise) {
        exercisesData.put(exercise.idExercise,exercise)
    }

    override suspend fun updateExercise(primaryKey: String, exercise: Exercise) {
        exercisesData[primaryKey] = exercise
    }

    override suspend fun removeExerciseById(primaryKey: String) {
        exercisesData.remove(primaryKey)
    }

    override suspend fun getExercises(): List<Exercise> {
        return ArrayList<Exercise>(exercisesData.values)
    }

    override suspend fun getTotalExercises(): Int {
        return exercisesData.size
    }

    override suspend fun getExerciseById(primaryKey: String): Exercise? {
        return exercisesData[primaryKey]
    }
}