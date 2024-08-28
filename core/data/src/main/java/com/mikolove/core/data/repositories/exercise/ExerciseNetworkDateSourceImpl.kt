package com.mikolove.core.data.repositories.exercise

import com.mikolove.core.domain.exercise.abstraction.ExerciseNetworkDataSource
import com.mikolove.core.domain.exercise.abstraction.ExerciseNetworkService
import com.mikolove.core.domain.exercise.Exercise


class ExerciseNetworkDateSourceImpl
constructor( private val exerciseNetworkService : ExerciseNetworkService)
    : ExerciseNetworkDataSource {
    override suspend fun insertExercise(exercise: Exercise)  = exerciseNetworkService.insertExercise(exercise)

    override suspend fun updateExercise(exercise: Exercise) = exerciseNetworkService.updateExercise(
        exercise
    )

    override suspend fun removeExerciseById(primaryKey: String)= exerciseNetworkService.removeExerciseById(primaryKey)

    override suspend fun getExercises(): List<Exercise> = exerciseNetworkService.getExercises()

    override suspend fun getExerciseById(primaryKey: String): Exercise? = exerciseNetworkService.getExerciseById(primaryKey)

    override suspend fun getExercisesByWorkout(idWorkout: String): List<Exercise>? = exerciseNetworkService.getExercisesByWorkout(idWorkout)

    override suspend fun getTotalExercises(): Int = exerciseNetworkService.getTotalExercises()

    override suspend fun addExerciseToWorkout(idWorkout: String, idExercise: String) = exerciseNetworkService.addExerciseToWorkout(idWorkout,idExercise)

    override suspend fun removeExerciseFromWorkout(idWorkout: String, idExercise: String) = exerciseNetworkService.removeExerciseFromWorkout(idWorkout,idExercise)

    override suspend fun isExerciseInWorkout(idWorkout: String, idExercise: String): Int = exerciseNetworkService.isExerciseInWorkout(idWorkout,idExercise)

    override suspend fun getDeletedExercises(): List<Exercise> = exerciseNetworkService.getDeletedExercises()

    override suspend fun insertDeletedExercise(exercise: Exercise) = exerciseNetworkService.insertDeletedExercise(exercise)

    override suspend fun insertDeletedExercises(exercises: List<Exercise>) = exerciseNetworkService.insertDeletedExercises(exercises)
}
