package com.mikolove.core.data.datasource.workouttype

import com.mikolove.core.domain.workouttype.abstraction.WorkoutTypeCacheDataSource
import com.mikolove.core.domain.workouttype.abstraction.WorkoutTypeCacheService
import com.mikolove.core.domain.workouttype.WorkoutType

class WorkoutTypeCacheDataSourceImpl
constructor( private val workoutTypeDaoService : WorkoutTypeCacheService) :
    WorkoutTypeCacheDataSource {

    override suspend fun upsertWorkoutType(workoutType: List<WorkoutType>): LongArray = workoutTypeDaoService.upsertWorkoutType(workoutType)

    override suspend fun getWorkoutTypeBydBodyPartId(idBodyPart: String): WorkoutType = workoutTypeDaoService.getWorkoutTypeBydBodyPartId(idBodyPart)

    override suspend fun removeWorkoutType(primaryKey: String): Int = workoutTypeDaoService.removeWorkoutType(primaryKey)

    override suspend fun getWorkoutTypes(): List<WorkoutType> = workoutTypeDaoService.getWorkoutTypes()

    override suspend fun getWorkoutTypeById(primaryKey: String): WorkoutType = workoutTypeDaoService. getWorkoutTypeById(primaryKey)

}
