package com.mikolove.allmightworkout.business.data.network.implementation

import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseNetworkDataSource
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.ExerciseFirestoreService


class ExerciseNetworkDateSourceImpl
constructor( private val exerciseFirestoreService : ExerciseFirestoreService)
    : ExerciseNetworkDataSource {
    override suspend fun insertExercise(exercise: Exercise)  = exerciseFirestoreService.insertExercise(exercise)

    override suspend fun updateExercise(exercise: Exercise) = exerciseFirestoreService.updateExercise(
        exercise
    )

    override suspend fun removeExerciseById(primaryKey: String)= exerciseFirestoreService.removeExerciseById(primaryKey)

    override suspend fun getExercises(): List<Exercise> = exerciseFirestoreService.getExercises()

    override suspend fun getExerciseById(primaryKey: String): Exercise? = exerciseFirestoreService.getExerciseById(primaryKey)

    override suspend fun getExercisesByWorkout(idWorkout: String): List<Exercise>? = exerciseFirestoreService.getExercisesByWorkout(idWorkout)

    override suspend fun getTotalExercises(): Int = exerciseFirestoreService.getTotalExercises()

    override suspend fun addExerciseToWorkout(idWorkout: String, idExercise: String) = exerciseFirestoreService.addExerciseToWorkout(idWorkout,idExercise)

    override suspend fun removeExerciseFromWorkout(idWorkout: String, idExercise: String) = exerciseFirestoreService.removeExerciseFromWorkout(idWorkout,idExercise)

    override suspend fun isExerciseInWorkout(idWorkout: String, idExercise: String): Int = exerciseFirestoreService.isExerciseInWorkout(idWorkout,idExercise)

    override suspend fun getDeletedExercises(): List<Exercise> = exerciseFirestoreService.getDeletedExercises()

    override suspend fun insertDeletedExercise(exercise: Exercise) = exerciseFirestoreService.insertDeletedExercise(exercise)

    override suspend fun insertDeletedExercises(exercises: List<Exercise>) = exerciseFirestoreService.insertDeletedExercises(exercises)
}
