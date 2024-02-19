package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.model.WorkoutGroup
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.WorkoutCacheEntity
import com.mikolove.allmightworkout.framework.datasource.cache.model.WorkoutGroupCacheEntity

class WorkoutGroupCacheMapper
constructor(
    private val dateUtil: DateUtil
)
    : EntityMapper<WorkoutGroupCacheEntity, WorkoutGroup> {

    override fun mapFromEntity(entity: WorkoutGroupCacheEntity): WorkoutGroup {
        return WorkoutGroup(
            idGroup = entity.idGroup,
            name = entity.name,
            createdAt = dateUtil.convertDateToStringDate(entity.createdAt),
            updatedAt = dateUtil.convertDateToStringDate(entity.updatedAt)
        )
    }

    override fun mapToEntity(domainModel: WorkoutGroup): WorkoutGroupCacheEntity {
        return WorkoutGroupCacheEntity(
            idGroup = domainModel.idGroup,
            name = domainModel.name,
            createdAt = dateUtil.convertStringDateToDate(domainModel.createdAt),
            updatedAt = dateUtil.convertStringDateToDate(domainModel.updatedAt)
        )
    }

}