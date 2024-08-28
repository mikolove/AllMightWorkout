package com.mikolove.core.domain.workout.abstraction

import com.mikolove.core.domain.workout.Group

interface GroupCacheDataSource {

    suspend fun getWorkoutGroups() : List<Group>

    suspend fun getGroup(groupId : String) : Group

    suspend fun upsertWorkoutGroup(group : Group) : Long

    suspend fun deleteWorkoutGroup(groups : Group) : Int
}