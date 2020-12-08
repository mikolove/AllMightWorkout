package com.mikolove.allmightworkout.framework.datasource.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.framework.datasource.database.EXERCISE_PAGINATION_PAGE_SIZE

interface ExerciseSetDaoService {

    suspend fun insertExerciseSet(exerciseSet: ExerciseSet) : Long

    suspend fun updateExerciseSet( primaryKey: String, reps: Int, weight: Int, time: Int, restTime: Int) : Int

    suspend fun removeExerciseSetById(primaryKey :String) : Int

}