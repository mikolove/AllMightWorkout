package com.mikolove.core.data.repositories.workout

import com.mikolove.core.domain.workout.Group
import com.mikolove.core.domain.workout.abstraction.GroupCacheDataSource
import com.mikolove.core.domain.workout.abstraction.GroupCacheService

class GroupCacheDataSourceImpl(
    private val groupCacheService : GroupCacheService
): GroupCacheDataSource {

    override suspend fun getGroups(idUser: String): List<Group> = groupCacheService.getGroups(idUser)

    override suspend fun getGroup(groupId: String): Group = groupCacheService.getGroup(groupId)

    override suspend fun upsertGroup(group: Group, idUser: String): Long = groupCacheService.upsertGroup(group,idUser)

    override suspend fun deleteGroups(groups: List<Group>,idUser : String): Int = groupCacheService.deleteGroups(groups,idUser)
}