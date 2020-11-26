package com.mikolove.allmightworkout.business.data.network.implementation

import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutTypeNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.WorkoutType
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.WorkoutTypeFirestoreService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutTypeNetworkDataSourceImpl
@Inject
constructor(private val workoutTypeFirestoreService : WorkoutTypeFirestoreService) : WorkoutTypeNetworkDataSource{

    override suspend fun getWorkoutTypes(): List<WorkoutType>  = workoutTypeFirestoreService.getWorkoutTypes()
}