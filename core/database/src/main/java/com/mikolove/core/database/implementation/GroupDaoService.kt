package com.mikolove.core.database.implementation

import com.mikolove.core.database.database.GroupDao
import com.mikolove.core.database.mappers.toGroup
import com.mikolove.core.database.mappers.toGroupCacheEntity
import com.mikolove.core.domain.workout.Group
import com.mikolove.core.domain.workout.abstraction.GroupCacheService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GroupDaoService(
    private val groupDao : GroupDao,
) : GroupCacheService {

    override suspend fun upsertGroup(group: Group, idUser: String): Long {
        return groupDao.upsertGroup(group.toGroupCacheEntity(idUser))
    }

    override suspend fun upsertGroups(group: List<Group>, idUser: String): List<Long> {
        return groupDao.upsertGroups(group.map { it.toGroupCacheEntity(idUser) })
    }

    override suspend fun deleteGroup(groupId: String, idUser: String): Int {
        return groupDao.deleteGroup(groupId,idUser)
    }

    override suspend fun deleteGroups(groupIds: List<String>, idUser: String): Int {
        return groupDao.deleteGroups(groupIds,idUser)
    }

    override fun getGroups(idUser: String): Flow<List<Group>> = flow {
        groupDao.getGroups(idUser).map { entities ->
            entities.map { it.toGroup() }
        }
    }

    override suspend fun getGroup(groupId: String): Group {
        return groupDao.getGroup(groupId).toGroup()
    }
}