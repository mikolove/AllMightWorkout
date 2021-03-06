package com.mikolove.allmightworkout.business.data.cache.implementation

import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutTypeCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.WorkoutType
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.WorkoutTypeDaoService
import javax.inject.Inject
import javax.inject.Singleton

class WorkoutTypeCacheDataSourceImpl
constructor( private val workoutTypeDaoService : WorkoutTypeDaoService) : WorkoutTypeCacheDataSource{

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
