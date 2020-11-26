package com.mikolove.allmightworkout.framework.datasource.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.WorkoutType
import com.mikolove.allmightworkout.framework.datasource.database.WORKOUTTYPE_PAGINATION_PAGE_SIZE

interface WorkoutTypeDaoService {

    suspend fun insertWorkoutTypes(workoutTypes: List<WorkoutType>) : Long

    suspend fun removeWorkoutTypes() : Int

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