package com.mikolove.core.domain.workouttype.abstraction

import com.mikolove.core.domain.workouttype.WorkoutType

interface WorkoutTypeCacheService {

    suspend fun insertWorkoutType(workoutTypes: WorkoutType) : Long

    suspend fun updateWorkoutType(idWorkoutType: String, name: String): Int

    suspend fun removeWorkoutType(primaryKey: String) : Int

    suspend fun getWorkoutTypeBydBodyPartId(idBodyPart: String?) : WorkoutType?

    suspend fun getWorkoutTypeById(primaryKey: String) : WorkoutType?

    suspend fun getWorkoutTypes() : List<WorkoutType>

    suspend fun getTotalWorkoutTypes() : Int

    suspend fun getWorkoutTypeOrderByNameDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<WorkoutType>

    suspend fun getWorkoutTypeOrderByNameASC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<WorkoutType>

    suspend fun returnOrderedQuery(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<WorkoutType>

}