package com.mikolove.core.database.implementation

import com.mikolove.core.database.database.WorkoutTypeDao
import com.mikolove.core.database.mappers.toWorkoutType
import com.mikolove.core.database.mappers.toWorkoutTypeCacheEntity
import com.mikolove.core.domain.workouttype.WorkoutType
import com.mikolove.core.domain.workouttype.abstraction.WorkoutTypeCacheService

class WorkoutTypeDaoService(
    private val workoutTypeDao : WorkoutTypeDao,
) : WorkoutTypeCacheService {

    override suspend fun upsertWorkoutType(workoutTypes: WorkoutType): Long {
        return workoutTypeDao.upsertWorkoutType(workoutTypes.toWorkoutTypeCacheEntity())
    }

    override suspend fun removeWorkoutType(primaryKey: String): Int {
        return workoutTypeDao.removeWorkoutType(primaryKey)
    }

    override suspend fun getWorkoutTypeBydBodyPartId(idBodyPart: String): WorkoutType {
        return workoutTypeDao.getWorkoutTypeBydBodyPartId(idBodyPart).toWorkoutType()
    }

    override suspend fun getWorkoutTypeById(primaryKey: String): WorkoutType {
        return workoutTypeDao.getWorkoutTypeById(primaryKey).toWorkoutType()
    }

    override suspend fun getWorkoutTypes(): List<WorkoutType> {
        return workoutTypeDao.getWorkoutTypes().map { it.toWorkoutType() }
    }
}