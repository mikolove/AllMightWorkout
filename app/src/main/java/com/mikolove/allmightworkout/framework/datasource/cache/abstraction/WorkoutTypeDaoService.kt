package com.mikolove.allmightworkout.framework.datasource.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.WorkoutType
import com.mikolove.allmightworkout.framework.datasource.cache.database.WORKOUTTYPE_PAGINATION_PAGE_SIZE

interface WorkoutTypeDaoService {

    suspend fun insertWorkoutType(workoutTypes: WorkoutType) : Long

    suspend fun updateWorkoutType(idWorkoutType: String, name: String): Int

    suspend fun removeWorkoutType(primaryKey: String) : Int

    suspend fun getAllWorkoutTypes(): List<WorkoutType>?

    suspend fun getWorkoutTypeBydBodyPartId(idBodyPart: String) : WorkoutType?

    suspend fun getWorkoutTypeById(primaryKey: String) : WorkoutType?

    suspend fun getWorkoutTypes() : List<WorkoutType>

    suspend fun getTotalWorkoutTypes() : Int

    suspend fun getWorkoutTypeOrderByNameDESC(
        query: String,
        page: Int,
        pageSize: Int = WORKOUTTYPE_PAGINATION_PAGE_SIZE
    ): List<WorkoutType>

    suspend fun getWorkoutTypeOrderByNameASC(
        query: String,
        page: Int,
        pageSize: Int = WORKOUTTYPE_PAGINATION_PAGE_SIZE
    ): List<WorkoutType>

    suspend fun returnOrderedQuery(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<WorkoutType>

}