package com.mikolove.core.domain.exercise

import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.model.Exercise

interface ExerciseCacheDataSource {

    suspend fun insertExercise(exercise: Exercise, idUser : String) : Long

    suspend fun updateExercise(
        primaryKey: String,
        name: String,
        bodyPart: BodyPart?,
        isActive: Boolean,
        exerciseType: String,
        updatedAt: String
    ) : Int

    suspend fun removeExerciseById(primaryKey :String) : Int

    suspend fun removeExercises(exercises: List<Exercise>)  : Int

    suspend fun getExercises(query : String, filterAndOrder : String, page : Int, idUser : String) : List<Exercise>

    suspend fun getExerciseById(primaryKey: String) : Exercise?

    suspend fun getExercisesByWorkout( idWorkout : String ) : List<Exercise>?

    suspend fun addExerciseToWorkout( idWorkout: String , idExercise: String) : Long

    suspend fun removeExerciseFromWorkout( idWorkout: String , idExercise: String) : Int

    suspend fun isExerciseInWorkout( idWorkout: String , idExercise: String ) : Int

    suspend fun getTotalExercises(idUser : String) : Int
}