package com.mikolove.allmightworkout.firebase.mappers

import com.mikolove.allmightworkout.firebase.model.HistoryExerciseNetworkEntity
import com.mikolove.core.domain.analytics.HistoryExercise
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.core.domain.util.EntityMapper

class HistoryExerciseNetworkMapper
constructor(
    private val dateUtil: DateUtil
) : EntityMapper<HistoryExerciseNetworkEntity, HistoryExercise> {

    override fun mapFromEntity(entity: HistoryExerciseNetworkEntity): HistoryExercise {
        return HistoryExercise(
            idHistoryExercise = entity.idHistoryExercise,
            name = entity.name,
            historySets = listOf(),
            bodyPart = entity.bodyPart,
            workoutType = entity.workoutType,
            exerciseType = entity.exerciseType,
            startedAt = dateUtil.convertFirebaseTimestampToStringData(entity.startedAt),
            endedAt = dateUtil.convertFirebaseTimestampToStringData(entity.endedAt),
            createdAt = dateUtil.convertFirebaseTimestampToStringData(entity.createdAt),
            updatedAt = dateUtil.convertFirebaseTimestampToStringData(entity.updatedAt)
        )
    }

    override fun mapToEntity(domainModel: HistoryExercise): HistoryExerciseNetworkEntity {

        return HistoryExerciseNetworkEntity(
            idHistoryExercise = domainModel.idHistoryExercise,
            name = domainModel.name,
            bodyPart = domainModel.bodyPart,
            workoutType = domainModel.workoutType,
            exerciseType = domainModel.exerciseType,
            startedAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.startedAt),
            endedAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.endedAt),
            createdAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.createdAt),
            updatedAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.updatedAt)
        )
    }
}