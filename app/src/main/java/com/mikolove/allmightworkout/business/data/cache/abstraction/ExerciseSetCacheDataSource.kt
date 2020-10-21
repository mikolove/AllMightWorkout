package com.mikolove.allmightworkout.business.data.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.ExerciseSet

interface ExerciseSetCacheDataSource {

    suspend fun insertExerciseSet(exerciseSet: ExerciseSet) : Long

    suspend fun updateExerciseSet(exerciseSet: ExerciseSet) : Int

    suspend fun removeExerciseSetById(primaryKey :Long) : Int

    suspend fun getExerciseSetByExerciseId(primaryKey: Long) : List<ExerciseSet>
}