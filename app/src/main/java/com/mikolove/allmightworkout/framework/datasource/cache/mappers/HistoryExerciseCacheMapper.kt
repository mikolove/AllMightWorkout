package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.core.domain.analytics.HistoryExercise
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.core.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.HistoryExerciseCacheEntity

class HistoryExerciseCacheMapper
constructor(
    private val dateUtil: DateUtil
) : EntityMapper<HistoryExerciseCacheEntity, HistoryExercise> {

    override fun mapFromEntity(entity: HistoryExerciseCacheEntity): HistoryExercise {
        return HistoryExercise(
            idHistoryExercise = entity.idHistoryExercise,
            name = entity.name,
            bodyPart = entity.bodyPart,
            workoutType = entity.workoutType,
            exerciseType = entity.exerciseType,
            historySets = listOf(),
            startedAt = dateUtil.convertDateToStringDate(entity.startedAt),
            endedAt = dateUtil.convertDateToStringDate(entity.endedAt),
            createdAt = dateUtil.convertDateToStringDate(entity.createdAt),
            updatedAt = dateUtil.convertDateToStringDate(entity.updatedAt)
        )
    }

    override fun mapToEntity(domainModel: HistoryExercise): HistoryExerciseCacheEntity {
        return HistoryExerciseCacheEntity(
            idHistoryExercise = domainModel.idHistoryExercise,
            idHistoryWorkout = null,
            name = domainModel.name,
            bodyPart = domainModel.bodyPart,
            workoutType = domainModel.workoutType,
            exerciseType = domainModel.exerciseType,
            startedAt = dateUtil.convertStringDateToDate(domainModel.startedAt),
            endedAt = dateUtil.convertStringDateToDate(domainModel.endedAt),
            createdAt = dateUtil.convertStringDateToDate(domainModel.createdAt),
            updatedAt = dateUtil.convertStringDateToDate(domainModel.updatedAt)

        )
    }

}