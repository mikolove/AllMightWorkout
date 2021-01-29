package com.mikolove.allmightworkout.business.data.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.WorkoutType

interface WorkoutTypeCacheDataSource {

    suspend fun insertWorkoutType(workoutType: WorkoutType) : Long

    suspend fun updateWorkoutType(idWorkoutType: String, name: String) : Int

    suspend fun removeWorkoutType(primaryKey: String) : Int

    suspend fun getAllWorkoutTypes() : List<WorkoutType>?

    suspend fun getWorkoutTypeBydBodyPartId(idBodyPart: String?) : WorkoutType?

    suspend fun getWorkoutTypes(query : String, filterAndOrder : String, page : Int) : List<WorkoutType>

    suspend fun getWorkoutTypeById(primaryKey: String) : WorkoutType?

    suspend fun getTotalWorkoutTypes() : Int
}

