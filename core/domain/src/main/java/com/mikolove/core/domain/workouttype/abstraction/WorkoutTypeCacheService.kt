package com.mikolove.core.domain.workouttype.abstraction

import com.mikolove.core.domain.workouttype.WorkoutType

interface WorkoutTypeCacheService {

    suspend fun upsertWorkoutType(workoutTypes: List<WorkoutType>) : LongArray

    suspend fun removeWorkoutType(primaryKey: String) : Int

    suspend fun getWorkoutTypes() : List<WorkoutType>

    suspend fun getWorkoutTypeBydBodyPartId(idBodyPart: String) : WorkoutType

    suspend fun getWorkoutTypeById(primaryKey: String) : WorkoutType

}