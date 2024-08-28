package com.mikolove.core.domain.workouttype.abstraction

import com.mikolove.core.domain.workouttype.WorkoutType

interface WorkoutTypeNetworkService {

    suspend fun getWorkoutTypes() : List<WorkoutType>

}