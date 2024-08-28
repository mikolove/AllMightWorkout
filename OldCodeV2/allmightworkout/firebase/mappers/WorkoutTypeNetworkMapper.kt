package com.mikolove.allmightworkout.firebase.mappers

import com.mikolove.allmightworkout.firebase.model.WorkoutTypeNetworkEntity
import com.mikolove.core.domain.util.EntityMapper
import com.mikolove.core.domain.workouttype.WorkoutType

class WorkoutTypeNetworkMapper : EntityMapper<WorkoutTypeNetworkEntity, WorkoutType> {

    override fun mapFromEntity(entity: WorkoutTypeNetworkEntity): WorkoutType {

        return WorkoutType(
            idWorkoutType = entity.idWorkoutType,
            name = entity.name,
        )
    }

    override fun mapToEntity(domainModel: WorkoutType): WorkoutTypeNetworkEntity {

        return WorkoutTypeNetworkEntity(
            idWorkoutType = domainModel.idWorkoutType,
            name = domainModel.name
        )
    }
}