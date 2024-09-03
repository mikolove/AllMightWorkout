package com.mikolove.core.domain.workouttype.abstraction

import com.mikolove.core.domain.workouttype.WorkoutType

interface   WorkoutTypeNetworkDataSource {

    suspend fun getWorkoutTypes(): List<WorkoutType>

}