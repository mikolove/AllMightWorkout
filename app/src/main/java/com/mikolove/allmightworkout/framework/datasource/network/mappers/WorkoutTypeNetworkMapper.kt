package com.mikolove.allmightworkout.framework.datasource.network.mappers

import com.mikolove.allmightworkout.business.domain.model.WorkoutType
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