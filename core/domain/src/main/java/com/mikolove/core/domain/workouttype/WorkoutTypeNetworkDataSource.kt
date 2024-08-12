package com.mikolove.core.domain.workouttype

import com.mikolove.allmightworkout.business.domain.model.WorkoutType

interface   WorkoutTypeNetworkDataSource {

    suspend fun getAllWorkoutTypes(): List<WorkoutType>

}