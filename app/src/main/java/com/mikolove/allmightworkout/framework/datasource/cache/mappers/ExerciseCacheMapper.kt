package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseType
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.ExerciseCacheEntity
import com.mikolove.allmightworkout.framework.datasource.cache.util.RoomDateUtil
import javax.inject.Inject

class ExerciseCacheMapper
@Inject
constructor(
    private val roomDateUtil: RoomDateUtil
) : EntityMapper<ExerciseCacheEntity,Exercise>{

    override fun mapFromEntity(entity: ExerciseCacheEntity): Exercise {
        return Exercise(
            idExercise = entity.idExercise,
            name = entity.name,
            sets = listOf(),
            bodyPart = null,
            exerciseType = ExerciseType.valueOf(entity.exerciseType),
            isActive = entity.isActive,
            startedAt = null,
            endedAt = null,
            createdAt = roomDateUtil.convertDateToStringDate(entity.createdAt),
            updatedAt = roomDateUtil.convertDateToStringDate(entity.updatedAt)
        )
    }

    override fun mapToEntity(domainModel: Exercise): ExerciseCacheEntity {
        return ExerciseCacheEntity(
            idExercise = domainModel.idExercise,
            name = domainModel.name,
            idBodyPart = domainModel.bodyPart?.idBodyPart,
            exerciseType = domainModel.exerciseType.name,
            isActive = domainModel.isActive,
            createdAt = roomDateUtil.convertStringDateToDate(domainModel.createdAt),
            updatedAt = roomDateUtil.convertStringDateToDate(domainModel.updatedAt)
        )
    }

}