package com.mikolove.allmightworkout.firebase.mappers

import com.mikolove.allmightworkout.firebase.model.ExerciseSetNetworkEntity
import com.mikolove.allmightworkout.util.toFirebaseTimestamp
import com.mikolove.allmightworkout.util.toZoneDateTime
import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.domain.util.EntityMapper

class ExerciseSetNetworkMapper
constructor(
) : EntityMapper<ExerciseSetNetworkEntity, ExerciseSet> {

    override fun mapFromEntity(entity: ExerciseSetNetworkEntity): ExerciseSet {
        return ExerciseSet(
            idExerciseSet = entity.idExerciseSet,
            reps = entity.reps,
            weight = entity.weight,
            time = entity.time,
            restTime = entity.restTime,
            order = entity.order,
            startedAt = null,
            endedAt = null,
            createdAt = entity.createdAt.toZoneDateTime(),
            updatedAt = entity.updatedAt.toZoneDateTime()
        )
    }

    override fun mapToEntity(domainModel: ExerciseSet): ExerciseSetNetworkEntity {
        return ExerciseSetNetworkEntity(
            idExerciseSet = domainModel.idExerciseSet,
            reps = domainModel.reps,
            weight = domainModel.weight,
            time = domainModel.time,
            restTime = domainModel.restTime,
            order = domainModel.order,
            createdAt = domainModel.createdAt.toFirebaseTimestamp(),
            updatedAt = domainModel.updatedAt.toFirebaseTimestamp()
        )
    }
}