package com.mikolove.core.data.workout

import com.mikolove.core.domain.workout.GroupCacheDataSource
import com.mikolove.core.domain.workout.Group
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.GroupDaoService

class GroupCacheDataSourceImpl(
    private val groupDaoService : GroupDaoService
): GroupCacheDataSource {


    override suspend fun getWorkoutGroups(): List<Group> = groupDaoService.getWorkoutGroups()

    override suspend fun getGroup(groupId: String): Group = groupDaoService.getGroup(groupId)

    override suspend fun upsertWorkoutGroup(group: Group): Long = groupDaoService.upsertWorkoutGroup(group)

    override suspend fun deleteWorkoutGroup(groups : Group): Int = groupDaoService.deleteWorkoutGroup(groups)
}