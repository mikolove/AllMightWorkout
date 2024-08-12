package com.mikolove.core.data.workouttype

import com.mikolove.core.domain.workouttype.WorkoutTypeCacheDataSource
import com.mikolove.core.domain.workouttype.WorkoutType
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.WorkoutTypeDaoService

class WorkoutTypeCacheDataSourceImpl
constructor( private val workoutTypeDaoService : WorkoutTypeDaoService) :
    WorkoutTypeCacheDataSource {

    override suspend fun insertWorkoutType(workoutType: WorkoutType): Long = workoutTypeDaoService.insertWorkoutType(workoutType)

    override suspend fun updateWorkoutType(idWorkoutType: String, name: String): Int = workoutTypeDaoService.updateWorkoutType(idWorkoutType,name)

    override suspend fun getWorkoutTypeBydBodyPartId(idBodyPart: String?): WorkoutType? = workoutTypeDaoService.getWorkoutTypeBydBodyPartId(idBodyPart)

    override suspend fun removeWorkoutType(primaryKey: String): Int = workoutTypeDaoService.removeWorkoutType(primaryKey)

    override suspend fun getWorkoutTypes(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<WorkoutType> {
       return workoutTypeDaoService.returnOrderedQuery(query, filterAndOrder, page)
    }

    override suspend fun getWorkoutTypeById(primaryKey: String): WorkoutType? = workoutTypeDaoService. getWorkoutTypeById(primaryKey)

    override suspend fun getTotalWorkoutTypes(): Int = workoutTypeDaoService.getTotalWorkoutTypes()

}
