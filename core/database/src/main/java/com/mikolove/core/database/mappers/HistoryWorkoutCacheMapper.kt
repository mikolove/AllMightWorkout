package com.mikolove.core.database.mappers

import com.mikolove.core.domain.analytics.HistoryWorkout
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.core.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.HistoryWorkoutCacheEntity

class HistoryWorkoutCacheMapper
constructor(
    private val dateUtil: DateUtil
) : EntityMapper<HistoryWorkoutCacheEntity, HistoryWorkout> {

    override fun mapFromEntity(entity: HistoryWorkoutCacheEntity): HistoryWorkout {
        return HistoryWorkout(
            idHistoryWorkout = entity.idHistoryWorkout,
            name = entity.name,
            historyExercises = null,
            startedAt = dateUtil.convertDateToStringDate(entity.startedAt),
            endedAt = dateUtil.convertDateToStringDate(entity.endedAt),
            createdAt = dateUtil.convertDateToStringDate(entity.createdAt),
            updatedAt = dateUtil.convertDateToStringDate(entity.updatedAt)
        )
    }

    override fun mapToEntity(domainModel: HistoryWorkout): HistoryWorkoutCacheEntity {
        return HistoryWorkoutCacheEntity(
            idHistoryWorkout = domainModel.idHistoryWorkout,
            name = domainModel.name,
            startedAt = dateUtil.convertStringDateToDate(domainModel.startedAt),
            endedAt = dateUtil.convertStringDateToDate(domainModel.endedAt),
            createdAt = dateUtil.convertStringDateToDate(domainModel.createdAt),
            updatedAt = dateUtil.convertStringDateToDate(domainModel.updatedAt)
        )
    }

}