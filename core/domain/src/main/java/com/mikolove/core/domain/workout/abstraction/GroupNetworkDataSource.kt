package com.mikolove.core.domain.workout.abstraction

import com.mikolove.core.domain.workout.Group

interface GroupNetworkDataSource {

    suspend fun getWorkoutGroups() : List<Group>

    suspend fun insertWorkoutGroup(group: Group)

    suspend fun updateWorkoutGroup(group: Group)

    suspend fun deleteWorkoutGroup(group : Group)

}