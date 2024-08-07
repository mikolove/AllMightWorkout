package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.core.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.WorkoutCacheEntity

class WorkoutCacheMapper
constructor(
    private val dateUtil: DateUtil
)
: EntityMapper<WorkoutCacheEntity, Workout> {

    override fun mapFromEntity(entity: WorkoutCacheEntity): Workout {
        return Workout(
            idWorkout = entity.idWorkout,
            name = entity.name,
            exercises = null,
            isActive = entity.isActive,
            exerciseIdsUpdatedAt = entity.exerciseIdsUpdatedAt?.let { dateUtil.convertDateToStringDate(it) },
            groups = null,
            startedAt = null,
            endedAt = null,
            createdAt = dateUtil.convertDateToStringDate(entity.createdAt),
            updatedAt = dateUtil.convertDateToStringDate(entity.updatedAt)
        )
    }

    override fun mapToEntity(domainModel: Workout): WorkoutCacheEntity {
        return WorkoutCacheEntity(
            idWorkout = domainModel.idWorkout,
            name = domainModel.name,
            isActive = domainModel.isActive,
            exerciseIdsUpdatedAt = domainModel.exerciseIdsUpdatedAt?.let{ dateUtil.convertStringDateToDate(it)},
            createdAt = dateUtil.convertStringDateToDate(domainModel.createdAt),
            updatedAt = dateUtil.convertStringDateToDate(domainModel.updatedAt)
        )
    }

}