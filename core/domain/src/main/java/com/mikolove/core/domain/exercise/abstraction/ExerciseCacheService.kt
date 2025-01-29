package com.mikolove.core.domain.exercise.abstraction

import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.core.domain.exercise.Exercise
import kotlinx.coroutines.flow.Flow

interface ExerciseCacheService {

    suspend fun upsertExercise(exercise: Exercise, idUser : String) : Long

    suspend fun removeExercises(exercises: List<Exercise>)  : Int

    fun getExercises(idUser : String) : Flow<List<Exercise>>

    suspend fun getExerciseById(primaryKey: String) : Exercise

    suspend fun isBodyPartInExercise( idExercise: String , idBodyPart: String ) : Boolean

    suspend fun addBodyPartToExercise(idExercise : String, idBodyPart : String) : Long

    suspend fun removeBodyPartFromExercise(idExercise : String, idBodyPart : String) : Int

}