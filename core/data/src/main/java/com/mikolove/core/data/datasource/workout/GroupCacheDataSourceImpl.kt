package com.mikolove.core.data.datasource.workout

import com.mikolove.core.domain.workout.Group
import com.mikolove.core.domain.workout.abstraction.GroupCacheDataSource
import com.mikolove.core.domain.workout.abstraction.GroupCacheService
import kotlinx.coroutines.flow.Flow

class GroupCacheDataSourceImpl(
    private val groupCacheService : GroupCacheService
): GroupCacheDataSource {

    override fun getGroups(idUser: String): Flow<List<Group>> = groupCacheService.getGroups(idUser)

    override suspend fun getGroup(groupId: String): Group = groupCacheService.getGroup(groupId)

    override suspend fun deleteGroups(groupIds: List<String>, idUser: String): Int = groupCacheService.deleteGroups(groupIds,idUser)

    override suspend fun deleteGroup(groupId: String, idUser: String): Int  = groupCacheService.deleteGroup(groupId,idUser)

    override suspend fun upsertGroup(group: Group, idUser: String): Long = groupCacheService.upsertGroup(group,idUser)

    override suspend fun upsertGroups(groups: List<Group>, idUser: String): List<Long> = groupCacheService.upsertGroups(groups, idUser)
}