package com.mikolove.core.database.implementation

import com.mikolove.core.database.database.GroupDao
import com.mikolove.core.database.mappers.toGroup
import com.mikolove.core.database.mappers.toGroupCacheEntity
import com.mikolove.core.domain.workout.Group
import com.mikolove.core.domain.workout.abstraction.GroupCacheService

class GroupDaoService(
    private val groupDao : GroupDao,
) : GroupCacheService {

    override suspend fun upsertGroup(group: Group, idUser: String): Long {
        return groupDao.upsertGroup(group.toGroupCacheEntity(idUser))
    }

    override suspend fun deleteGroups(groups: List<Group>,idUser : String): Int {
        val groupCacheEntities = groups.map { it.toGroupCacheEntity(idUser)}
        return groupDao.deleteGroups(groupCacheEntities)
    }

    override suspend fun getGroups(idUser: String): List<Group> {
        return groupDao.getGroups(idUser).map { it.toGroup() }
    }

    override suspend fun getGroup(groupId: String): Group {
        return groupDao.getGroup(groupId).toGroup()
    }
}