package com.mikolove.core.data.datasource.exercise

import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.abstraction.ExerciseCacheDataSource
import com.mikolove.core.domain.exercise.abstraction.ExerciseCacheService
import kotlinx.coroutines.flow.Flow

class ExerciseCacheDataSourceImpl
constructor(
    private val exerciseCacheService : ExerciseCacheService,
)
    : ExerciseCacheDataSource {

    override suspend fun upsertExercise(exercise: Exercise, idUser: String): Long = exerciseCacheService.upsertExercise(exercise, idUser)

    override fun getExercises(idUser: String) : Flow<List<Exercise>> = exerciseCacheService.getExercises(idUser)

    override suspend fun removeExercises(exercises: List<Exercise>): Int = exerciseCacheService.removeExercises(exercises)

    override suspend fun getExerciseById(primaryKey: String): Exercise = exerciseCacheService.getExerciseById(primaryKey)

    override suspend fun isBodyPartInExercise(idExercise: String, idBodyPart: String): Boolean = exerciseCacheService.isBodyPartInExercise( idExercise, idBodyPart)

    override suspend fun addBodyPartToExercise(idExercise: String, idBodyPart: String): Long = exerciseCacheService.addBodyPartToExercise(idExercise, idBodyPart)

    override suspend fun removeBodyPartFromExercise(idExercise: String, idBodyPart: String): Int = exerciseCacheService.removeBodyPartFromExercise(idExercise, idBodyPart)

}