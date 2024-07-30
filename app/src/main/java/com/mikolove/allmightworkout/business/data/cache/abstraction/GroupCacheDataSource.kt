package com.mikolove.allmightworkout.business.data.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.Group

interface GroupCacheDataSource {

    suspend fun getWorkoutGroups() : List<Group>

    suspend fun getGroup(groupId : String) : Group

    suspend fun insertWorkoutGroup(group : Group) : Long

    suspend fun updateWorkoutGroup(group : Group) : Int

    suspend fun deleteWorkoutGroup(groups : Group) : Int
}