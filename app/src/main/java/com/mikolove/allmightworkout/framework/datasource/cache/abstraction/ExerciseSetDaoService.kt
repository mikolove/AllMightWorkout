package com.mikolove.allmightworkout.framework.datasource.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.ExerciseSet

interface ExerciseSetDaoService {

    suspend fun insertExerciseSet(exerciseSet: ExerciseSet, idExercise: String) : Long

    suspend fun updateExerciseSet( primaryKey: String, reps: Int, weight: Int, time: Int, restTime: Int, updatedAt : String, idExercise: String ) : Int

    suspend fun removeExerciseSets(exerciseSets: List<ExerciseSet>) : Int

    suspend fun removeExerciseSetById(primaryKey :String, idExercise: String) : Int

    suspend fun getExerciseSetById(primaryKey: String, idExercise: String) : ExerciseSet?

    suspend fun getExerciseSetByIdExercise(idExercise : String) : List<ExerciseSet>?

    suspend fun getTotalExercisesSetByExercise(idExercise: String) : Int
}