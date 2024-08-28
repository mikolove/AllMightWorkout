package com.mikolove.core.domain.workout.abstraction

import com.mikolove.core.domain.workout.Group

interface GroupCacheService {

    suspend fun upsertWorkoutGroup(group : Group) : Long

    suspend fun getWorkoutGroups() : List<Group>

    suspend fun getGroup(groupId : String) : Group

    suspend fun deleteWorkoutGroup(groups : Group) : Int
}