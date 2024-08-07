package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.core.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.ExerciseSetCacheEntity
import javax.inject.Inject

class ExerciseSetCacheMapper
@Inject
constructor(
    private val dateUtil: DateUtil
): EntityMapper<ExerciseSetCacheEntity, ExerciseSet> {
    override fun mapFromEntity(entity: ExerciseSetCacheEntity): ExerciseSet {
        return ExerciseSet(
            idExerciseSet = entity.idExerciseSet,
            reps = entity.reps,
            weight = entity.weight,
            time = entity.time,
            restTime = entity.restTime,
            order = entity.order,
            startedAt = null,
            endedAt = null,
            createdAt = dateUtil.convertDateToStringDate(entity.createdAt),
            updatedAt = dateUtil.convertDateToStringDate(entity.updatedAt)
        )
    }

    override fun mapToEntity(domainModel: ExerciseSet): ExerciseSetCacheEntity {
        return ExerciseSetCacheEntity(
            idExerciseSet = domainModel.idExerciseSet,
            idExercise = null,
            reps = domainModel.reps,
            weight = domainModel.weight,
            time = domainModel.time,
            restTime = domainModel.restTime,
            order = domainModel.order,
            createdAt = dateUtil.convertStringDateToDate(domainModel.createdAt),
            updatedAt = dateUtil.convertStringDateToDate(domainModel.updatedAt)
        )
    }

}