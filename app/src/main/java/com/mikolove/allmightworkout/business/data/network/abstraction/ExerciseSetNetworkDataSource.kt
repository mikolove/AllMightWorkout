package com.mikolove.allmightworkout.business.data.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.ExerciseSet

interface ExerciseSetNetworkDataSource {

    suspend fun insertExerciseSet(exerciseSet: ExerciseSet, idExercise : String)

    suspend fun updateExerciseSet(exerciseSet: ExerciseSet, idExercise: String)

    suspend fun removeExerciseSetById(primaryKey :String, idExercise: String)

    suspend fun getExerciseSetById(primaryKey: String, idExercise: String) : ExerciseSet?

    suspend fun getExerciseSetByIdExercise(idExercise : String) : List<ExerciseSet>?

    suspend fun getTotalExercisesSetByExercise(idExercise: String) : Int

    suspend fun insertDeletedExerciseSet(exerciseSet: ExerciseSet)

    suspend fun getDeletedExerciseSets() : List<ExerciseSet>
}