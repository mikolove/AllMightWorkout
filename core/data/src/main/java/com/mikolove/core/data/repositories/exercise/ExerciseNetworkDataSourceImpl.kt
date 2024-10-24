package com.mikolove.core.data.repositories.exercise

import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.abstraction.ExerciseNetworkDataSource
import com.mikolove.core.domain.exercise.abstraction.ExerciseNetworkService

class ExerciseNetworkDataSourceImpl
constructor( private val exerciseNetworkService : ExerciseNetworkService)
    : ExerciseNetworkDataSource {

    override suspend fun upsertExercise(exercise: Exercise) = exerciseNetworkService.upsertExercise(exercise)


    override suspend fun removeExercise(primaryKey: String) = exerciseNetworkService.removeExercise(primaryKey)


    override suspend fun getExercises(): List<Exercise> = exerciseNetworkService.getExercises()

}
