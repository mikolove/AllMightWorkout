package com.mikolove.core.database.implementation

import com.mikolove.core.domain.workouttype.WorkoutType
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.WorkoutTypeDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.database.WorkoutTypeDao
import com.mikolove.allmightworkout.framework.datasource.cache.database.returnOrderedQuery
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.WorkoutTypeCacheMapper
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.WorkoutTypeWithBodyPartCacheMapper

class WorkoutTypeDaoServiceImpl
constructor(
    private val workoutTypeDao : WorkoutTypeDao,
    private val workoutTypeCacheMapper: WorkoutTypeCacheMapper,
    private val workoutTypeWithBodyPartCacheMapper: WorkoutTypeWithBodyPartCacheMapper
) : WorkoutTypeDaoService{

    override suspend fun insertWorkoutType(workoutTypes: WorkoutType): Long {
        return workoutTypeDao.insertWorkoutType(workoutTypeCacheMapper.mapToEntity(workoutTypes))
    }

    override suspend fun updateWorkoutType(idWorkoutType: String, name: String): Int {
        return workoutTypeDao.updateWorkoutType(
            idWorkoutType = idWorkoutType,
            name = name
        )
    }

    override suspend fun removeWorkoutType(primaryKey: String): Int {
        return workoutTypeDao.removeWorkoutType(primaryKey)
    }

    override suspend fun getWorkoutTypeBydBodyPartId(idBodyPart: String?): WorkoutType? {
        return workoutTypeDao.getWorkoutTypeBydBodyPartId(idBodyPart)?.let {
            workoutTypeWithBodyPartCacheMapper.mapFromEntity(it)

        }
    }

    override suspend fun getWorkoutTypeById(primaryKey: String): WorkoutType? {
        return workoutTypeDao.getWorkoutTypeById(primaryKey)?.let {
            workoutTypeWithBodyPartCacheMapper.mapFromEntity(it)
        }
    }

    override suspend fun getWorkoutTypes(): List<WorkoutType> {
        return workoutTypeWithBodyPartCacheMapper.entityListToDomainList(
            workoutTypeDao.getWorkoutTypes()
        )
    }

    override suspend fun getTotalWorkoutTypes(): Int {
        return workoutTypeDao.getTotalWorkoutTypes()
    }

    override suspend fun getWorkoutTypeOrderByNameDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<WorkoutType> {
        return workoutTypeWithBodyPartCacheMapper.entityListToDomainList(
            workoutTypeDao.getWorkoutTypeOrderByNameDESC(query, page)
        )
    }

    override suspend fun getWorkoutTypeOrderByNameASC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<WorkoutType> {
        return workoutTypeWithBodyPartCacheMapper.entityListToDomainList(
            workoutTypeDao.getWorkoutTypeOrderByNameASC(query, page)
        )
    }

    override suspend fun returnOrderedQuery(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<WorkoutType> {
        return workoutTypeWithBodyPartCacheMapper.entityListToDomainList(
            workoutTypeDao.returnOrderedQuery(query, filterAndOrder, page)
        )
    }
}