package com.mikolove.core.data.workouttype

import com.mikolove.core.domain.workouttype.WorkoutTypeNetworkDataSource
import com.mikolove.core.domain.workouttype.WorkoutType
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.WorkoutTypeFirestoreService

class WorkoutTypeNetworkDataSourceImpl
constructor(private val workoutTypeFirestoreService : WorkoutTypeFirestoreService) :
    WorkoutTypeNetworkDataSource {

    override suspend fun getAllWorkoutTypes(): List<WorkoutType> = workoutTypeFirestoreService.getWorkoutTypes()
}