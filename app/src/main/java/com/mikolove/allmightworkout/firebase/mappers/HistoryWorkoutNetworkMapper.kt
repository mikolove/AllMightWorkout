package com.mikolove.allmightworkout.firebase.mappers

import com.mikolove.allmightworkout.firebase.model.HistoryWorkoutNetworkEntity
import com.mikolove.allmightworkout.util.toFirebaseTimestamp
import com.mikolove.allmightworkout.util.toZoneDateTime
import com.mikolove.core.domain.analytics.HistoryWorkout
import com.mikolove.core.domain.util.EntityMapper

class HistoryWorkoutNetworkMapper
constructor(
) : EntityMapper<HistoryWorkoutNetworkEntity, HistoryWorkout> {

    override fun mapFromEntity(entity: HistoryWorkoutNetworkEntity): HistoryWorkout {

        return HistoryWorkout(
            idHistoryWorkout = entity.idHistoryWorkout,
            name = entity.name,
            historyExercises = listOf(),
            startedAt = entity.startedAt.toZoneDateTime(),
            endedAt = entity.endedAt.toZoneDateTime(),
            createdAt = entity.createdAt.toZoneDateTime(),
        )
    }

    override fun mapToEntity(domainModel: HistoryWorkout): HistoryWorkoutNetworkEntity {
        return HistoryWorkoutNetworkEntity(
            idHistoryWorkout = domainModel.idHistoryWorkout,
            name = domainModel.name,
            startedAt = domainModel.startedAt.toFirebaseTimestamp(),
            endedAt = domainModel.endedAt.toFirebaseTimestamp(),
            createdAt = domainModel.createdAt.toFirebaseTimestamp(),
            )
    }
}