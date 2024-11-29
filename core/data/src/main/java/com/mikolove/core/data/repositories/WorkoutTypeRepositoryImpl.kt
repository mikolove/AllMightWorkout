package com.mikolove.core.data.repositories

import com.mikolove.core.data.util.safeCacheCall
import com.mikolove.core.domain.util.DataError
import com.mikolove.core.domain.util.Result
import com.mikolove.core.domain.workouttype.WorkoutType
import com.mikolove.core.domain.workouttype.WorkoutTypeRepository
import com.mikolove.core.domain.workouttype.abstraction.WorkoutTypeCacheDataSource

class WorkoutTypeRepositoryImpl(
    private val workoutTypeCacheDataSource: WorkoutTypeCacheDataSource
): WorkoutTypeRepository{

    override suspend fun getWorkoutTypes(): Result<List<WorkoutType>,DataError> {
        return safeCacheCall {
            workoutTypeCacheDataSource.getWorkoutTypes()
        }
    }
}