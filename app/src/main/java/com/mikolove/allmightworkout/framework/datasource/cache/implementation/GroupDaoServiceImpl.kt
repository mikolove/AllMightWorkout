package com.mikolove.allmightworkout.framework.datasource.cache.implementation

import com.mikolove.allmightworkout.business.domain.model.Group
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.GroupDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.database.GroupDao
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.GroupCacheMapper

class GroupDaoServiceImpl
constructor(
    private val groupDao : GroupDao,
    private val groupCacheMapper : GroupCacheMapper,
    private val groupWithWorkouts : GroupWithWorkoutsCacheMapper,
    private val dateUtil: DateUtil,
) : GroupDaoService {

    override suspend fun getWorkoutGroups(): List<Group>{
        return groupDao.getWorkoutGroups().let {
            groupCacheMapper.entityListToDomainList(it)
        }
    }

    override suspend fun getGroup(groupId: String): Group {
        return groupDao.getGroup(groupId).let{
            groupCacheMapper.mapFromEntity(it)
        }
    }

    override suspend fun insertWorkoutGroup(group: Group): Long {
        val entity = groupCacheMapper.mapToEntity(group)
       return groupDao.insertWorkoutGroup(entity)
    }

    override suspend fun updateWorkoutGroup(group: Group): Int {
        val updatedAt = group.copy(updatedAt = dateUtil.getCurrentTimestamp())
        val entity = groupCacheMapper.mapToEntity(updatedAt)
        return groupDao.updateWorkoutGroup(entity)
    }

    override suspend fun deleteWorkoutGroup(groups: Group): Int {
        val entity= groupCacheMapper.mapToEntity(groups)
        return groupDao.deleteWorkoutGroup(entity)
    }
}