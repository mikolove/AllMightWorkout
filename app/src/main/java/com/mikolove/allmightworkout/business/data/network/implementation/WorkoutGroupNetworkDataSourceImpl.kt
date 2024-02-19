package com.mikolove.allmightworkout.business.data.network.implementation

import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutGroupNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.WorkoutGroup
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.WorkoutGroupFirestoreService

class WorkoutGroupNetworkDataSourceImpl(
    private val workoutGroupFirestoreService : WorkoutGroupFirestoreService
) : WorkoutGroupNetworkDataSource{
    override suspend fun getWorkoutGroups(): List<WorkoutGroup> = workoutGroupFirestoreService.getWorkoutGroups()

    override suspend fun insertWorkoutGroup(workoutGroup: WorkoutGroup) = workoutGroupFirestoreService.insertWorkoutGroup(workoutGroup)

    override suspend fun updateWorkoutGroup(workoutGroup: WorkoutGroup) = workoutGroupFirestoreService.updateWorkoutGroup(workoutGroup)
    override suspend fun deleteWorkoutGroup(group: WorkoutGroup) = workoutGroupFirestoreService.deleteWorkoutGroup(group)
}