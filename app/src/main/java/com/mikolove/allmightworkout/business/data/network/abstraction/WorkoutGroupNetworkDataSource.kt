package com.mikolove.allmightworkout.business.data.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.WorkoutGroup

interface WorkoutGroupNetworkDataSource {

    suspend fun getWorkoutGroups() : List<WorkoutGroup>

    suspend fun insertWorkoutGroup(workoutGroup: WorkoutGroup)

    suspend fun updateWorkoutGroup(workoutGroup: WorkoutGroup)

    suspend fun deleteWorkoutGroup(group : WorkoutGroup)

}