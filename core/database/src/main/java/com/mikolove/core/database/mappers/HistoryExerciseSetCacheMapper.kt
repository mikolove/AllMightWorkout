package com.mikolove.core.database.mappers

import com.mikolove.core.domain.analytics.HistoryExerciseSet
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.core.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.HistoryExerciseSetCacheEntity

class HistoryExerciseSetCacheMapper
constructor(
    private val dateUtil: DateUtil
) : EntityMapper<HistoryExerciseSetCacheEntity, HistoryExerciseSet> {

    override fun mapFromEntity(entity: HistoryExerciseSetCacheEntity): HistoryExerciseSet {
        return HistoryExerciseSet(
            idHistoryExerciseSet = entity.idHistoryExerciseSet,
            reps = entity.reps,
            weight = entity.weight,
            time = entity.time,
            restTime = entity.restTime,
            startedAt = dateUtil.convertDateToStringDate(entity.startedAt),
            endedAt = dateUtil.convertDateToStringDate(entity.endedAt),
            createdAt = dateUtil.convertDateToStringDate(entity.createdAt),
            updatedAt = dateUtil.convertDateToStringDate(entity.updatedAt)
        )
    }

    override fun mapToEntity(domainModel: HistoryExerciseSet): HistoryExerciseSetCacheEntity {
        return HistoryExerciseSetCacheEntity(
            idHistoryExerciseSet = domainModel.idHistoryExerciseSet,
            idHistoryExercise = null,
            reps = domainModel.reps,
            weight = domainModel.weight,
            time = domainModel.time,
            restTime = domainModel.restTime,
            startedAt = dateUtil.convertStringDateToDate(domainModel.startedAt),
            endedAt = dateUtil.convertStringDateToDate(domainModel.endedAt),
            createdAt = dateUtil.convertStringDateToDate(domainModel.createdAt),
            updatedAt = dateUtil.convertStringDateToDate(domainModel.updatedAt)
        )
    }

}