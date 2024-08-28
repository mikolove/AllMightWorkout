package com.mikolove.core.data.repositories.workout

import com.mikolove.core.domain.workout.abstraction.GroupCacheDataSource
import com.mikolove.core.domain.workout.abstraction.GroupCacheService
import com.mikolove.core.domain.workout.Group

class GroupCacheDataSourceImpl(
    private val groupCacheService : GroupCacheService
): GroupCacheDataSource {


    override suspend fun getWorkoutGroups(): List<Group> = groupCacheService.getWorkoutGroups()

    override suspend fun getGroup(groupId: String): Group = groupCacheService.getGroup(groupId)

    override suspend fun upsertWorkoutGroup(group: Group): Long = groupCacheService.upsertWorkoutGroup(group)

    override suspend fun deleteWorkoutGroup(groups : Group): Int = groupCacheService.deleteWorkoutGroup(groups)
}