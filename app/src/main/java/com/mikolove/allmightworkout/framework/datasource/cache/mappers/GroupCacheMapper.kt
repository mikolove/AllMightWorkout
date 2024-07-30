package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.allmightworkout.business.domain.model.Group
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.GroupCacheEntity

class GroupCacheMapper
constructor(
    private val dateUtil: DateUtil
)
    : EntityMapper<GroupCacheEntity, Group> {

    override fun mapFromEntity(entity: GroupCacheEntity): Group {
        return Group(
            idGroup = entity.idGroup,
            name = entity.name,
            workouts = null,
            createdAt = dateUtil.convertDateToStringDate(entity.createdAt),
            updatedAt = dateUtil.convertDateToStringDate(entity.updatedAt)
        )
    }

    override fun mapToEntity(domainModel: Group): GroupCacheEntity {
        return GroupCacheEntity(
            idGroup = domainModel.idGroup,
            name = domainModel.name,

            createdAt = dateUtil.convertStringDateToDate(domainModel.createdAt),
            updatedAt = dateUtil.convertStringDateToDate(domainModel.updatedAt)
        )
    }

}