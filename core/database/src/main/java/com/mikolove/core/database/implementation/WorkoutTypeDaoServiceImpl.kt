package com.mikolove.core.database.implementation

import com.mikolove.core.database.database.WorkoutTypeDao
import com.mikolove.core.database.mappers.WorkoutTypeCacheMapper
import com.mikolove.core.database.mappers.WorkoutTypeWithBodyPartCacheMapper
import com.mikolove.core.domain.workouttype.WorkoutType
import com.mikolove.core.domain.workouttype.abstraction.WorkoutTypeCacheService

class WorkoutTypeDaoServiceImpl
constructor(
    private val workoutTypeDao : WorkoutTypeDao,
    private val workoutTypeCacheMapper: WorkoutTypeCacheMapper,
    private val workoutTypeWithBodyPartCacheMapper: WorkoutTypeWithBodyPartCacheMapper
) : WorkoutTypeCacheService {

    override suspend fun upsertWorkoutType(workoutTypes: WorkoutType): Long {
        return workoutTypeDao.insertWorkoutType(workoutTypeCacheMapper.mapToEntity(workoutTypes))
    }


    override suspend fun removeWorkoutType(primaryKey: String): Int {
        return workoutTypeDao.removeWorkoutType(primaryKey)
    }

    override suspend fun getWorkoutTypeBydBodyPartId(idBodyPart: String?): WorkoutType? {
        return workoutTypeDao.getWorkoutTypeBydBodyPartId(idBodyPart)?.let {
            workoutTypeWithBodyPartCacheMapper.mapFromEntity(it)

        }
    }

    override suspend fun getWorkoutTypeById(primaryKey: String): WorkoutType? {
        return workoutTypeDao.getWorkoutTypeById(primaryKey)?.let {
            workoutTypeWithBodyPartCacheMapper.mapFromEntity(it)
        }
    }

    override suspend fun getWorkoutTypes(): List<WorkoutType> {
        return workoutTypeWithBodyPartCacheMapper.entityListToDomainList(
            workoutTypeDao.getWorkoutTypes()
        )
    }
}