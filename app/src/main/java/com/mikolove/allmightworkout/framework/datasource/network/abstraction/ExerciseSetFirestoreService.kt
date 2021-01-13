package com.mikolove.allmightworkout.framework.datasource.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.ExerciseSet

interface ExerciseSetFirestoreService {
    
    suspend fun insertExerciseSet(exerciseSet: ExerciseSet, idExercise : String)

    suspend fun updateExerciseSet(primaryKey: String, exerciseSet: ExerciseSet, idExercise : String)

    suspend fun removeExerciseSetById(primaryKey :String , idExercise : String)

    suspend fun getExerciseSetById(primaryKey: String, idExercise: String) : ExerciseSet?

    suspend fun getExerciseSetByIdExercise(idExercise : String) : List<ExerciseSet>?

}