package com.mikolove.core.database.mappers

import com.mikolove.core.database.model.BodyPartCacheEntity
import com.mikolove.core.database.model.BodyPartCacheEntityWithWorkoutType
import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.core.domain.util.EntityMapper

class BodyPartCacheMapper(
    private val workoutTypeCacheMapper: WorkoutTypeCacheMapper
)  : EntityMapper<BodyPartCacheEntityWithWorkoutType, BodyPart> {

    override fun mapFromEntity(entity: BodyPartCacheEntityWithWorkoutType): BodyPart {
        return BodyPart(
            idBodyPart = entity.bodyPart.idBodyPart,
            name = entity.bodyPart.name,
            workoutType = workoutTypeCacheMapper.mapFromEntity(entity.workoutTypeCacheEntity)
        )
    }

    override fun mapToEntity(domainModel: BodyPart): BodyPartCacheEntityWithWorkoutType {
        return BodyPartCacheEntityWithWorkoutType(

            bodyPart = domainModel,
            workoutTypeCacheEntity =
        )
    }

}