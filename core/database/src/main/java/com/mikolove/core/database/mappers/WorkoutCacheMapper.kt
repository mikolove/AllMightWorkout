package com.mikolove.core.database.mappers

import com.mikolove.core.database.model.WorkoutCacheEntity
import com.mikolove.core.domain.util.EntityMapper
import com.mikolove.core.domain.workout.Workout

class WorkoutCacheMapper
: EntityMapper<WorkoutCacheEntity, Workout> {

    override fun mapFromEntity(entity: WorkoutCacheEntity): Workout {
        return Workout(
            idWorkout = entity.idWorkout,
            name = entity.name,
            exercises = listOf(),
            isActive = entity.isActive,
            exerciseIdsUpdatedAt = entity.exerciseIdsUpdatedAt,
            groups = listOf(),
            startedAt = null,
            endedAt = null,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt)

    }

    override fun mapToEntity(domainModel: Workout): WorkoutCacheEntity {
        return WorkoutCacheEntity(
            idWorkout = domainModel.idWorkout,
            name = domainModel.name,
            isActive = domainModel.isActive,
            exerciseIdsUpdatedAt = domainModel.exerciseIdsUpdatedAt,
            createdAt = domainModel.createdAt,
            updatedAt = domainModel.updatedAt
        )
    }

}