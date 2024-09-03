package com.mikolove.core.data.repositories.workouttype

import com.mikolove.core.domain.workouttype.abstraction.WorkoutTypeNetworkDataSource
import com.mikolove.core.domain.workouttype.abstraction.WorkoutTypeNetworkService
import com.mikolove.core.domain.workouttype.WorkoutType

class WorkoutTypeNetworkDataSourceImpl
constructor(private val workoutTypeFirestoreService : WorkoutTypeNetworkService) :
    WorkoutTypeNetworkDataSource {

    override suspend fun getWorkoutTypes(): List<WorkoutType> = workoutTypeFirestoreService.getWorkoutTypes()
}