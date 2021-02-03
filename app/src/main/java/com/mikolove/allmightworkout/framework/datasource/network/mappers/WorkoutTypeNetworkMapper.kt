package com.mikolove.allmightworkout.framework.datasource.network.mappers

import com.mikolove.allmightworkout.business.domain.model.WorkoutType
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.WorkoutTypeNetworkEntity

class WorkoutTypeNetworkMapper
constructor(
    private val bodyPartNetworkMapper: BodyPartNetworkMapper
):EntityMapper<WorkoutTypeNetworkEntity,WorkoutType>{

    override fun mapFromEntity(entity: WorkoutTypeNetworkEntity): WorkoutType {

        val bodyParts = entity.bodyParts?.let { bodyPartNetworkMapper.entityListToDomainList(it) }

        return WorkoutType(
            idWorkoutType = entity.idWorkoutType,
            name = entity.name,
            bodyParts = bodyParts
        )
    }

    override fun mapToEntity(domainModel: WorkoutType): WorkoutTypeNetworkEntity {
        val bodyParts = domainModel.bodyParts?.let { bodyPartNetworkMapper.domainListToEntityList(it) }
        return WorkoutTypeNetworkEntity(
            idWorkoutType = domainModel.idWorkoutType,
            name = domainModel.name,
            bodyParts = bodyParts
        )
    }
}