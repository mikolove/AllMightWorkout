package com.mikolove.allmightworkout.framework.datasource.cache.implementation

import com.mikolove.allmightworkout.business.domain.model.WorkoutGroup
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.WorkoutGroupDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.database.WorkoutGroupDao
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.WorkoutGroupCacheMapper

class WorkoutGroupDaoServiceImpl
constructor(
    private val workoutGroupDao : WorkoutGroupDao,
    private val workoutGroupCacheMapper : WorkoutGroupCacheMapper,
    private val dateUtil: DateUtil,
) : WorkoutGroupDaoService {

    override suspend fun getWorkoutGroups(): List<WorkoutGroup>{
        return workoutGroupDao.getWorkoutGroups().let {
            workoutGroupCacheMapper.entityListToDomainList(it)
        }
    }

    override suspend fun insertWorkoutGroup(workoutGroup: WorkoutGroup): Long {
        val entity = workoutGroupCacheMapper.mapToEntity(workoutGroup)
       return workoutGroupDao.insertWorkoutGroup(entity)
    }

    override suspend fun updateWorkoutGroup(workoutGroup: WorkoutGroup): Int {
        val updatedAt = workoutGroup.copy(updatedAt = dateUtil.getCurrentTimestamp())
        val entity = workoutGroupCacheMapper.mapToEntity(updatedAt)
        return workoutGroupDao.updateWorkoutGroup(entity)
    }

    override suspend fun deleteWorkoutGroup(workoutGroups: WorkoutGroup): Int {
        val entity= workoutGroupCacheMapper.mapToEntity(workoutGroups)
        return workoutGroupDao.deleteWorkoutGroup(entity)
    }
}