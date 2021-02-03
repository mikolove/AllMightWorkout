package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.allmightworkout.business.domain.model.HistoryExerciseSet
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.HistoryExerciseSetCacheEntity
import com.mikolove.allmightworkout.framework.datasource.cache.util.RoomDateUtil
import javax.inject.Inject

class HistoryExerciseSetCacheMapper
constructor(
    private val roomDateUtil: RoomDateUtil
) : EntityMapper<HistoryExerciseSetCacheEntity,HistoryExerciseSet>{

    override fun mapFromEntity(entity: HistoryExerciseSetCacheEntity): HistoryExerciseSet {
        return HistoryExerciseSet(
            idHistoryExerciseSet = entity.idHistoryExerciseSet,
            reps = entity.reps,
            weight = entity.weight,
            time = entity.time,
            restTime = entity.restTime,
            startedAt = roomDateUtil.convertDateToStringDate(entity.startedAt),
            endedAt = roomDateUtil.convertDateToStringDate(entity.endedAt),
            createdAt = roomDateUtil.convertDateToStringDate(entity.createdAt),
            updatedAt = roomDateUtil.convertDateToStringDate(entity.updatedAt)
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
            startedAt = roomDateUtil.convertStringDateToDate(domainModel.startedAt),
            endedAt = roomDateUtil.convertStringDateToDate(domainModel.endedAt),
            createdAt = roomDateUtil.convertStringDateToDate(domainModel.createdAt),
            updatedAt = roomDateUtil.convertStringDateToDate(domainModel.updatedAt)
        )
    }

}