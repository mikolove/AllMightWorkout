package com.mikolove.allmightworkout.framework.datasource.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.ExerciseSet

interface ExerciseSetFirestoreService {
    
    suspend fun insertExerciseSet(exerciseSet: ExerciseSet)

    suspend fun updateExerciseSet(primaryKey: String, exerciseSet: ExerciseSet)

    suspend fun removeExerciseSetById(primaryKey :String)

    suspend fun getExerciseSetByExerciseId(primaryKey: String) : List<ExerciseSet>

}