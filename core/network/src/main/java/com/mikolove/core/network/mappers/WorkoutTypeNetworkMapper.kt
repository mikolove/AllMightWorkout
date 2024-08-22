package com.mikolove.core.network.mappers

import com.mikolove.core.domain.workouttype.WorkoutType
import com.mikolove.core.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.WorkoutTypeNetworkEntity

class WorkoutTypeNetworkMapper : EntityMapper<WorkoutTypeNetworkEntity, WorkoutType> {

    override fun mapFromEntity(entity: WorkoutTypeNetworkEntity): WorkoutType {

        return WorkoutType(
            idWorkoutType = entity.idWorkoutType,
            name = entity.name,
            bodyParts = null
        )
    }

    override fun mapToEntity(domainModel: WorkoutType): WorkoutTypeNetworkEntity {

        return WorkoutTypeNetworkEntity(
            idWorkoutType = domainModel.idWorkoutType,
            name = domainModel.name
        )
    }
}