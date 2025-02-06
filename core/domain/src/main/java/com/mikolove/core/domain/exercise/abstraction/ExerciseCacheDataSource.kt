package com.mikolove.core.domain.exercise.abstraction

import com.mikolove.core.domain.exercise.Exercise
import kotlinx.coroutines.flow.Flow


interface ExerciseCacheDataSource {

    suspend fun upsertExercise(exercise: Exercise, idUser : String) : Long

    suspend fun upsertExercises(exercises: List<Exercise>, idUser : String) : List<Long>

    suspend fun removeExercise( primaryKey: String)  : Int

    suspend fun removeExercises(primaryKeys: List<String>)  : List<Int>

    fun getExercises(idUser : String) : Flow<List<Exercise>>

    suspend fun getExerciseById(primaryKey: String) : Exercise

    suspend fun isBodyPartInExercise( idExercise: String , idBodyPart: String ) : Boolean

    suspend fun addBodyPartToExercise(idExercise : String, idBodyPart : String) : Long

    suspend fun removeBodyPartFromExercise(idExercise : String, idBodyPart : String) : Int
}