package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.ExerciseType
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.core.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.ExerciseCacheEntity
import javax.inject.Inject

class ExerciseCacheMapper
@Inject
constructor(
    private val dateUtil: DateUtil
) : EntityMapper<ExerciseCacheEntity, Exercise> {

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
            createdAt = dateUtil.convertDateToStringDate(entity.createdAt),
            updatedAt = dateUtil.convertDateToStringDate(entity.updatedAt)
        )
    }

    override fun mapToEntity(domainModel: Exercise): ExerciseCacheEntity {
        return ExerciseCacheEntity(
            idExercise = domainModel.idExercise,
            name = domainModel.name,
            idBodyPart = domainModel.bodyPart?.idBodyPart,
            exerciseType = domainModel.exerciseType.name,
            isActive = domainModel.isActive,
            createdAt = dateUtil.convertStringDateToDate(domainModel.createdAt),
            updatedAt = dateUtil.convertStringDateToDate(domainModel.updatedAt)
        )
    }

}