package com.mikolove.allmightworkout.business.data.cache.implementation

import com.mikolove.allmightworkout.business.data.cache.abstraction.GroupCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.Group
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.GroupDaoService

class GroupCacheDataSourceImpl(
    private val groupDaoService : GroupDaoService
): GroupCacheDataSource{


    override suspend fun getWorkoutGroups(): List<Group> = groupDaoService.getWorkoutGroups()

    override suspend fun getGroup(groupId: String): Group = groupDaoService.getGroup(groupId)

    override suspend fun upsertWorkoutGroup(group: Group): Long = groupDaoService.upsertWorkoutGroup(group)

    override suspend fun deleteWorkoutGroup(groups : Group): Int = groupDaoService.deleteWorkoutGroup(groups)
}