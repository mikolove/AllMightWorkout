package com.mikolove.allmightworkout.framework.datasource.network.mappers

import com.mikolove.allmightworkout.business.domain.model.WorkoutGroup
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.WorkoutGroupNetworkEntity

class WorkoutGroupNetworkMapper(
    private val dateUtil: DateUtil
) : EntityMapper<WorkoutGroupNetworkEntity,WorkoutGroup>{

    override fun mapFromEntity(entity: WorkoutGroupNetworkEntity): WorkoutGroup {
        return WorkoutGroup(
            idGroup = entity.idWorkoutGroup,
            name = entity.name,
            createdAt = dateUtil.convertFirebaseTimestampToStringData(entity.createdAt),
            updatedAt = dateUtil.convertFirebaseTimestampToStringData(entity.updatedAt),
        )
    }

    override fun mapToEntity(domainModel: WorkoutGroup): WorkoutGroupNetworkEntity {
        return WorkoutGroupNetworkEntity(
            idWorkoutGroup = domainModel.idGroup,
            name = domainModel.name,
            createdAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.createdAt),
            updatedAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.updatedAt)
        )
    }

    override fun entityListToDomainList(entities: List<WorkoutGroupNetworkEntity>): List<WorkoutGroup> {
        return super.entityListToDomainList(entities)
    }

    override fun domainListToEntityList(domains: List<WorkoutGroup>): List<WorkoutGroupNetworkEntity> {
        return super.domainListToEntityList(domains)
    }
}