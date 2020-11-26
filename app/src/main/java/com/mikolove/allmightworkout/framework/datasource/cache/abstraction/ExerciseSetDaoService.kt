package com.mikolove.allmightworkout.framework.datasource.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.framework.datasource.database.EXERCISE_PAGINATION_PAGE_SIZE

interface ExerciseSetDaoService {

    suspend fun insertExerciseSet(exerciseSet: ExerciseSet) : Long

    suspend fun updateExerciseSet(primaryKey: String, exerciseSet: ExerciseSet) : Int

    suspend fun removeExerciseSetById(primaryKey :String) : Int

    suspend fun getExerciseSetsByExerciseId(idExercise: String) : List<ExerciseSet>

    suspend fun getTotalExerciseSet() : Int
}