package com.mikolove.allmightworkout.business.data.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.WorkoutType

interface WorkoutTypeCacheDataSource {

    suspend fun insertWorkoutTypes(workoutTypes: List<WorkoutType>) : Long

    suspend fun removeWorkoutTypes() : Int

    suspend fun getWorkoutTypes(query : String, filterAndOrder : String, page : Int) : List<WorkoutType>

    suspend fun getWorkoutTypeById(primaryKey: String) : WorkoutType?

    suspend fun getTotalWorkoutTypes() : Int
}

