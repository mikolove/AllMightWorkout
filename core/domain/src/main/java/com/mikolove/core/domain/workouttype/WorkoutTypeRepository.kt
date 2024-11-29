package com.mikolove.core.domain.workouttype

import com.mikolove.core.domain.util.DataError
import com.mikolove.core.domain.util.Result
interface WorkoutTypeRepository {
    suspend fun getWorkoutTypes() : Result<List<WorkoutType>,DataError>
}