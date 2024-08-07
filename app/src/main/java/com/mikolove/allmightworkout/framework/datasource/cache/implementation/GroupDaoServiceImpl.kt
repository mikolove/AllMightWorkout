package com.mikolove.allmightworkout.framework.datasource.cache.implementation

import com.mikolove.allmightworkout.business.domain.model.Group
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.GroupDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.database.GroupDao
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.GroupCacheMapper
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.GroupsWithWorkoutsCacheMapper

class GroupDaoServiceImpl
constructor(
    private val groupDao : GroupDao,
    private val groupCacheMapper : GroupCacheMapper,
    private val groupWithWorkouts : GroupsWithWorkoutsCacheMapper,
    private val dateUtil: DateUtil,
) : GroupDaoService {

    override suspend fun getWorkoutGroups(): List<Group>{
        return groupDao.getWorkoutGroups().let {
            groupWithWorkouts.entityListToDomainList(it)
        }
    }

    override suspend fun getGroup(groupId: String): Group {
        return groupDao.getGroup(groupId).let{
            groupWithWorkouts.mapFromEntity(it)
        }
    }

    override suspend fun deleteWorkoutGroup(groups: Group): Int {
        val entity= groupCacheMapper.mapToEntity(groups)
        return groupDao.deleteWorkoutGroup(entity)
    }

    override suspend fun upsertWorkoutGroup(group: Group) : Long {
        val entity= groupCacheMapper.mapToEntity(group)
        return groupDao.upsertWorkoutGroup(entity)
    }
}