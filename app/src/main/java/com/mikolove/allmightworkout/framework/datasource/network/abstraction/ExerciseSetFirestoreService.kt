package com.mikolove.allmightworkout.framework.datasource.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.ExerciseSet

interface ExerciseSetFirestoreService {
    
    suspend fun insertExerciseSet(exerciseSet: ExerciseSet, idExercise : String)

    suspend fun updateExerciseSet(exerciseSet: ExerciseSet, idExercise: String)

    suspend fun updateExerciseSets(exerciseSet: List<ExerciseSet>, idExercise: String)

    suspend fun removeExerciseSetById(primaryKey :String , idExercise : String)

    suspend fun removeExerciseSetsById(primaryKeys :List<String> , idExercise : String)

    suspend fun getExerciseSetById(primaryKey: String, idExercise: String) : ExerciseSet?

    suspend fun getExerciseSetByIdExercise(idExercise : String) : List<ExerciseSet>?

    suspend fun getTotalExercisesSetByExercise(idExercise: String) : Int

    suspend fun insertDeletedExerciseSet(exerciseSet: ExerciseSet)

    suspend fun insertDeletedExerciseSets(exerciseSet: List<ExerciseSet>)

    suspend fun getDeletedExerciseSets() : List<ExerciseSet>

}