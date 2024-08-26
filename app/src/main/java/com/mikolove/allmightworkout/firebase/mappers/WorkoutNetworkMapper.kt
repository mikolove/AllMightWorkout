package com.mikolove.allmightworkout.firebase.mappers

import com.mikolove.allmightworkout.firebase.model.WorkoutNetworkEntity
import com.mikolove.allmightworkout.util.toFirebaseTimestamp
import com.mikolove.allmightworkout.util.toZoneDateTime
import com.mikolove.core.domain.util.EntityMapper
import com.mikolove.core.domain.workout.Workout

class WorkoutNetworkMapper
    : EntityMapper<WorkoutNetworkEntity, Workout> {

    override fun mapFromEntity(entity: WorkoutNetworkEntity): Workout {
        return Workout(
            idWorkout = entity.idWorkout,
            name = entity.name,
            isActive = entity.isActive,
            startedAt = null,
            endedAt = null,
            exerciseIdsUpdatedAt = entity.exerciseIdsUpdatedAt?.let { it.toZoneDateTime() },
            createdAt = entity.createdAt.toZoneDateTime(),
            updatedAt = entity.updatedAt.toZoneDateTime()
        )
    }

    override fun mapToEntity(domainModel: Workout): WorkoutNetworkEntity {
        return WorkoutNetworkEntity(
            idWorkout = domainModel.idWorkout,
            name = domainModel.name,
            exerciseIds = null,
            exerciseIdsUpdatedAt = domainModel.exerciseIdsUpdatedAt?.let { it.toFirebaseTimestamp() },
            groupIds = null,
            isActive = domainModel.isActive,
            createdAt = domainModel.createdAt.toFirebaseTimestamp(),
            updatedAt = domainModel.updatedAt.toFirebaseTimestamp()
        )
    }

}