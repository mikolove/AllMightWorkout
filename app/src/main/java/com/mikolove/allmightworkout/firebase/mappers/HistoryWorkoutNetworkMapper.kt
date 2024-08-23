package com.mikolove.allmightworkout.firebase.mappers

import com.mikolove.allmightworkout.firebase.model.HistoryWorkoutNetworkEntity
import com.mikolove.core.domain.analytics.HistoryWorkout
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.core.domain.util.EntityMapper

class HistoryWorkoutNetworkMapper
constructor(
    private val dateUtil: DateUtil
) : EntityMapper<HistoryWorkoutNetworkEntity, HistoryWorkout> {

    override fun mapFromEntity(entity: HistoryWorkoutNetworkEntity): HistoryWorkout {

        return HistoryWorkout(
            idHistoryWorkout = entity.idHistoryWorkout,
            name = entity.name,
            historyExercises = null,
            startedAt = dateUtil.convertFirebaseTimestampToStringData(entity.startedAt),
            endedAt = dateUtil.convertFirebaseTimestampToStringData(entity.endedAt),
            createdAt = dateUtil.convertFirebaseTimestampToStringData(entity.createdAt),
            updatedAt = dateUtil.convertFirebaseTimestampToStringData(entity.updatedAt)
        )
    }

    override fun mapToEntity(domainModel: HistoryWorkout): HistoryWorkoutNetworkEntity {
        return HistoryWorkoutNetworkEntity(
            idHistoryWorkout = domainModel.idHistoryWorkout,
            name = domainModel.name,
            startedAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.startedAt),
            endedAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.endedAt),
            createdAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.createdAt),
            updatedAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.updatedAt)
        )
    }
}