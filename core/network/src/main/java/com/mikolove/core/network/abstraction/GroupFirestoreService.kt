package com.mikolove.core.network.abstraction

import com.mikolove.core.domain.workout.Group

interface GroupFirestoreService {

    suspend fun getWorkoutGroups() : List<Group>

    suspend fun insertWorkoutGroup(group: Group)

    suspend fun updateWorkoutGroup(group: Group)

    suspend fun deleteWorkoutGroup(group : Group)
}