package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.ExerciseSetCacheEntity
import com.mikolove.allmightworkout.framework.datasource.cache.util.RoomDateUtil
import javax.inject.Inject

class ExerciseSetCacheMapper
@Inject
constructor(
    private val roomDateUtil: RoomDateUtil
): EntityMapper<ExerciseSetCacheEntity,ExerciseSet>{
    override fun mapFromEntity(entity: ExerciseSetCacheEntity): ExerciseSet {
        return ExerciseSet(
            idExerciseSet = entity.idExerciseSet,
            reps = entity.reps,
            weight = entity.weight,
            time = entity.time,
            restTime = entity.restTime,
            startedAt = null,
            endedAt = null,
            createdAt = roomDateUtil.convertDateToStringDate(entity.createdAt),
            updatedAt = roomDateUtil.convertDateToStringDate(entity.updatedAt)
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
            createdAt = roomDateUtil.convertStringDateToDate(domainModel.createdAt),
            updatedAt = roomDateUtil.convertStringDateToDate(domainModel.updatedAt)
        )
    }

}