package com.mikolove.core.domain.exercise.abstraction

import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.core.domain.exercise.Exercise
import kotlinx.coroutines.flow.Flow


interface ExerciseCacheDataSource {

    suspend fun upsertExercise(exercise: Exercise, idUser : String) : Long

    suspend fun upsertExercises(exercises: List<Exercise>, idUser : String) : List<Long>

    suspend fun removeExercise( primaryKey: String)  : Int

    suspend fun removeExercises(primaryKeys: List<String>)  : Int

    fun getExercises(idUser : String,searchQuery : String) : Flow<List<Exercise>>

    fun getExercisesByWorkoutTypes(workoutTypes : List<String>, idUser : String) : Flow<List<Exercise>>

    suspend fun getExerciseById(primaryKey: String) : Exercise

    suspend fun isBodyPartInExercise( idExercise: String , idBodyPart: String ) : Boolean

    //suspend fun addBodyPartToExercise(idExercise : String, idBodyPart : String) : Long

    //suspend fun removeBodyPartsFromExercise(idExercise : String) : Int

    suspend fun insertBodyPartsAndClean(idExercise: String, bodyParts: List<BodyPart>)

}