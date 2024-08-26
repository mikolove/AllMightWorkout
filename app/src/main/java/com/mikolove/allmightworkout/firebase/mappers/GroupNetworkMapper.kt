package com.mikolove.allmightworkout.firebase.mappers

import com.mikolove.allmightworkout.firebase.model.GroupNetworkEntity
import com.mikolove.allmightworkout.util.toFirebaseTimestamp
import com.mikolove.allmightworkout.util.toZoneDateTime
import com.mikolove.core.domain.util.EntityMapper
import com.mikolove.core.domain.workout.Group

class GroupNetworkMapper : EntityMapper<GroupNetworkEntity, Group> {

    override fun mapFromEntity(entity: GroupNetworkEntity): Group {
        return Group(
            idGroup = entity.idWorkoutGroup,
            name = entity.name,
            workouts = listOf(),
            createdAt = entity.createdAt.toZoneDateTime(),
            updatedAt = entity.updatedAt.toZoneDateTime(),
        )
    }

    override fun mapToEntity(domainModel: Group): GroupNetworkEntity {
        return GroupNetworkEntity(
            idWorkoutGroup = domainModel.idGroup,
            name = domainModel.name,
            createdAt = domainModel.createdAt.toFirebaseTimestamp(),
            updatedAt = domainModel.updatedAt.toFirebaseTimestamp()
        )
    }

    override fun entityListToDomainList(entities: List<GroupNetworkEntity>): List<Group> {
        return super.entityListToDomainList(entities)
    }

    override fun domainListToEntityList(domains: List<Group>): List<GroupNetworkEntity> {
        return super.domainListToEntityList(domains)
    }
}