package com.mikolove.core.data.repositories.exercise

import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.abstraction.ExerciseCacheDataSource
import com.mikolove.core.domain.exercise.abstraction.ExerciseCacheService
import kotlinx.coroutines.flow.Flow

class ExerciseCacheDataSourceImpl
constructor(
    private val exerciseDaoService : ExerciseCacheService,
)
    : ExerciseCacheDataSource {

    override suspend fun upsertExercise(exercise: Exercise, idUser: String): Long = exerciseDaoService.upsertExercise(exercise, idUser)

    override suspend fun getExercises(idUser: String) : Flow<List<Exercise>> = exerciseDaoService.getExercises(idUser)

    override suspend fun removeExercises(exercises: List<Exercise>): Int = exerciseDaoService.removeExercises(exercises)

    override suspend fun getExerciseById(primaryKey: String): Exercise = exerciseDaoService.getExerciseById(primaryKey)

}