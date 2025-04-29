package com.mikolove.core.data.datasource.exercise

import com.mikolove.core.domain.bodypart.BodyPart
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

    override suspend fun upsertExercises(exercises: List<Exercise>, idUser: String): List<Long> = exerciseCacheService.upsertExercises(exercises,idUser)

    override fun getExercises(idUser: String, searchQuery : String) : Flow<List<Exercise>> = exerciseCacheService.getExercises(idUser,searchQuery)

    override fun getExercisesByWorkoutTypes(workoutTypes: List<String>, idUser: String): Flow<List<Exercise>> = exerciseCacheService.getExercisesByWorkoutTypes(workoutTypes, idUser)

    override suspend fun removeExercise(primaryKey: String): Int = exerciseCacheService.removeExercise(primaryKey)

    override suspend fun removeExercises(primaryKeys: List<String>): Int = exerciseCacheService.removeExercises(primaryKeys)

    override suspend fun getExerciseById(primaryKey: String): Exercise = exerciseCacheService.getExerciseById(primaryKey)

    override suspend fun isBodyPartInExercise(idExercise: String, idBodyPart: String): Boolean = exerciseCacheService.isBodyPartInExercise( idExercise, idBodyPart)

    //override suspend fun addBodyPartToExercise(idExercise: String, idBodyPart: String): Long = exerciseCacheService.addBodyPartToExercise(idExercise, idBodyPart)

    //override suspend fun removeBodyPartsFromExercise(idExercise: String): Int = exerciseCacheService.removeBodyPartsFromExercise(idExercise)

    override suspend fun insertBodyPartsAndClean(idExercise: String, bodyParts: List<BodyPart>) = exerciseCacheService.insertBodyPartsAndClean(idExercise,bodyParts)
}