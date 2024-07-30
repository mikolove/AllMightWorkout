package com.mikolove.allmightworkout.framework.datasource.network.mappers

import com.mikolove.allmightworkout.business.domain.model.Group
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.GroupNetworkEntity

class GroupNetworkMapper(
    private val dateUtil: DateUtil
) : EntityMapper<GroupNetworkEntity,Group>{

    override fun mapFromEntity(entity: GroupNetworkEntity): Group {
        return Group(
            idGroup = entity.idWorkoutGroup,
            name = entity.name,
            createdAt = dateUtil.convertFirebaseTimestampToStringData(entity.createdAt),
            updatedAt = dateUtil.convertFirebaseTimestampToStringData(entity.updatedAt),
        )
    }

    override fun mapToEntity(domainModel: Group): GroupNetworkEntity {
        return GroupNetworkEntity(
            idWorkoutGroup = domainModel.idGroup,
            name = domainModel.name,
            createdAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.createdAt),
            updatedAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.updatedAt)
        )
    }

    override fun entityListToDomainList(entities: List<GroupNetworkEntity>): List<Group> {
        return super.entityListToDomainList(entities)
    }

    override fun domainListToEntityList(domains: List<Group>): List<GroupNetworkEntity> {
        return super.domainListToEntityList(domains)
    }
}