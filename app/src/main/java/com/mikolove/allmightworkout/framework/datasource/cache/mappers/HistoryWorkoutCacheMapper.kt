package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.HistoryWorkoutCacheEntity
import com.mikolove.allmightworkout.framework.datasource.cache.util.RoomDateUtil
import javax.inject.Inject

class HistoryWorkoutCacheMapper
constructor(
    private val roomDateUtil: RoomDateUtil
) : EntityMapper<HistoryWorkoutCacheEntity, HistoryWorkout>{

    override fun mapFromEntity(entity: HistoryWorkoutCacheEntity): HistoryWorkout {
        return HistoryWorkout(
            idHistoryWorkout = entity.idHistoryWorkout,
            name = entity.name,
            historyExercises = listOf(),
            startedAt = roomDateUtil.convertDateToStringDate(entity.startedAt),
            endedAt = roomDateUtil.convertDateToStringDate(entity.endedAt),
            createdAt = roomDateUtil.convertDateToStringDate(entity.createdAt),
            updatedAt = roomDateUtil.convertDateToStringDate(entity.updatedAt)
        )
    }

    override fun mapToEntity(domainModel: HistoryWorkout): HistoryWorkoutCacheEntity {
        return HistoryWorkoutCacheEntity(
            idHistoryWorkout = domainModel.idHistoryWorkout,
            name = domainModel.name,
            startedAt = roomDateUtil.convertStringDateToDate(domainModel.startedAt),
            endedAt = roomDateUtil.convertStringDateToDate(domainModel.endedAt),
            createdAt = roomDateUtil.convertStringDateToDate(domainModel.createdAt),
            updatedAt = roomDateUtil.convertStringDateToDate(domainModel.updatedAt)
        )
    }

}