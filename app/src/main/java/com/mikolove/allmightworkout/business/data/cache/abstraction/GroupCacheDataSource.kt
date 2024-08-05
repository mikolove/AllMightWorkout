package com.mikolove.allmightworkout.business.data.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.Group

interface GroupCacheDataSource {

    suspend fun getWorkoutGroups() : List<Group>

    suspend fun getGroup(groupId : String) : Group

    suspend fun upsertWorkoutGroup(group : Group) : Long

    suspend fun deleteWorkoutGroup(groups : Group) : Int
}