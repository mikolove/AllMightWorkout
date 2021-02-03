package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.allmightworkout.business.domain.model.HistoryExercise
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.HistoryExerciseCacheEntity
import com.mikolove.allmightworkout.framework.datasource.cache.util.RoomDateUtil
import javax.inject.Inject

class HistoryExerciseCacheMapper
constructor(
    private val roomDateUtil: RoomDateUtil
) : EntityMapper<HistoryExerciseCacheEntity, HistoryExercise>{

    override fun mapFromEntity(entity: HistoryExerciseCacheEntity): HistoryExercise {
        return HistoryExercise(
            idHistoryExercise = entity.idHistoryExercise,
            name = entity.name,
            bodyPart = entity.bodyPart,
            workoutType = entity.workoutType,
            exerciseType = entity.exerciseType,
            historySets = listOf(),
            startedAt = roomDateUtil.convertDateToStringDate(entity.startedAt),
            endedAt = roomDateUtil.convertDateToStringDate(entity.endedAt),
            createdAt = roomDateUtil.convertDateToStringDate(entity.createdAt),
            updatedAt = roomDateUtil.convertDateToStringDate(entity.updatedAt)
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
            startedAt = roomDateUtil.convertStringDateToDate(domainModel.startedAt),
            endedAt = roomDateUtil.convertStringDateToDate(domainModel.endedAt),
            createdAt = roomDateUtil.convertStringDateToDate(domainModel.createdAt),
            updatedAt = roomDateUtil.convertStringDateToDate(domainModel.updatedAt)

        )
    }

}