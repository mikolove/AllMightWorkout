package com.mikolove.core.network.firebase.mappers

import com.mikolove.allmightworkout.firebase.model.HistoryExerciseNetworkEntity
import com.mikolove.allmightworkout.util.toFirebaseTimestamp
import com.mikolove.allmightworkout.util.toZoneDateTime
import com.mikolove.core.domain.analytics.HistoryExercise
import com.mikolove.core.domain.util.EntityMapper

class HistoryExerciseNetworkMapper
constructor(
) : EntityMapper<HistoryExerciseNetworkEntity, HistoryExercise> {

    override fun mapFromEntity(entity: HistoryExerciseNetworkEntity): HistoryExercise {
        return HistoryExercise(
            idHistoryExercise = entity.idHistoryExercise,
            name = entity.name,
            historySets = listOf(),
            bodyPart = entity.bodyPart,
            workoutType = entity.workoutType,
            exerciseType = entity.exerciseType,
            startedAt = entity.startedAt.toZoneDateTime(),
            endedAt = entity.endedAt.toZoneDateTime(),
            createdAt = entity.createdAt.toZoneDateTime(),
        )
    }

    override fun mapToEntity(domainModel: HistoryExercise): HistoryExerciseNetworkEntity {

        return HistoryExerciseNetworkEntity(
            idHistoryExercise = domainModel.idHistoryExercise,
            name = domainModel.name,
            bodyPart = domainModel.bodyPart,
            workoutType = domainModel.workoutType,
            exerciseType = domainModel.exerciseType,
            startedAt = domainModel.startedAt.toFirebaseTimestamp(),
            endedAt = domainModel.endedAt.toFirebaseTimestamp(),
            createdAt = domainModel.createdAt.toFirebaseTimestamp(),
        )
    }
}