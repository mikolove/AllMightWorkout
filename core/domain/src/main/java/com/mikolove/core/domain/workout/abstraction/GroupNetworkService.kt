package com.mikolove.core.domain.workout.abstraction

import com.mikolove.core.domain.workout.Group

interface GroupNetworkService {

    suspend fun upsertGroup(group: Group)

    suspend fun deleteWorkoutGroup(idGroup : String)

    suspend fun getWorkoutGroups() : List<Group>
}