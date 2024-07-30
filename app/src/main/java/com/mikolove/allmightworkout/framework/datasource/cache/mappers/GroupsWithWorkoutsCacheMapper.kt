package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.allmightworkout.business.domain.model.Group
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.GroupCacheEntity
import com.mikolove.allmightworkout.framework.datasource.cache.model.GroupsWithWorkoutsCacheEntity

class GroupsWithWorkoutsCacheMapper
constructor(
    private val workoutWithExercisesCacheMapper: WorkoutWithExercisesCacheMapper,
    private val dateUtil: DateUtil
)
    : EntityMapper<GroupsWithWorkoutsCacheEntity, Group> {

    override fun mapFromEntity(entity: GroupsWithWorkoutsCacheEntity): Group {
        return Group(
            idGroup = entity.groupCacheEntity.idGroup,
            name = entity.groupCacheEntity.name,
            workouts = entity.listOfWorkoutsCacheEntity?.let {
                workoutWithExercisesCacheMapper.entityListToDomainList(it)
            },
            createdAt = dateUtil.convertDateToStringDate(entity.groupCacheEntity.createdAt),
            updatedAt = dateUtil.convertDateToStringDate(entity.groupCacheEntity.updatedAt)
        )
    }

    override fun mapToEntity(domainModel: Group): GroupsWithWorkoutsCacheEntity {
        TODO("Not yet implemented")
    }

}