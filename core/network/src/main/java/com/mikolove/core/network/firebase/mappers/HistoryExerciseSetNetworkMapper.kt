package com.mikolove.core.network.firebase.mappers

import com.mikolove.allmightworkout.firebase.model.HistoryExerciseSetNetworkEntity
import com.mikolove.allmightworkout.util.toFirebaseTimestamp
import com.mikolove.allmightworkout.util.toZoneDateTime
import com.mikolove.core.domain.analytics.HistoryExerciseSet
import com.mikolove.core.domain.util.EntityMapper

class HistoryExerciseSetNetworkMapper : EntityMapper<HistoryExerciseSetNetworkEntity, HistoryExerciseSet> {

    override fun mapFromEntity(entity: HistoryExerciseSetNetworkEntity): HistoryExerciseSet {
        return HistoryExerciseSet(
            idHistoryExerciseSet = entity.idHistoryExerciseSet,
            reps = entity.reps,
            weight = entity.weight,
            time = entity.time,
            restTime = entity.restTime,
            startedAt = entity.startedAt.toZoneDateTime(),
            endedAt = entity.endedAt.toZoneDateTime(),
            createdAt = entity.createdAt.toZoneDateTime(),
        )
    }

    override fun mapToEntity(domainModel: HistoryExerciseSet): HistoryExerciseSetNetworkEntity {
        return HistoryExerciseSetNetworkEntity(
            idHistoryExerciseSet = domainModel.idHistoryExerciseSet,
            reps = domainModel.reps,
            weight = domainModel.weight,
            time = domainModel.time,
            restTime = domainModel.restTime,
            startedAt = domainModel.startedAt.toFirebaseTimestamp(),
            endedAt = domainModel.endedAt.toFirebaseTimestamp(),
            createdAt = domainModel.createdAt.toFirebaseTimestamp(),
        )
    }
}