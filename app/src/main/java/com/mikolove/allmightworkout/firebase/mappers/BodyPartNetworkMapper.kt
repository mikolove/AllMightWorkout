package com.mikolove.allmightworkout.firebase.mappers

import com.mikolove.allmightworkout.firebase.model.BodyPartNetworkEntity
import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.core.domain.util.EntityMapper

class BodyPartNetworkMapper : EntityMapper<BodyPartNetworkEntity, BodyPart> {

    override fun mapFromEntity(entity: BodyPartNetworkEntity): BodyPart {
        return BodyPart(
            idBodyPart = entity.idBodyPart,
            name = entity.name
        )
    }

    override fun mapToEntity(domainModel: BodyPart): BodyPartNetworkEntity {
        return BodyPartNetworkEntity(
            idBodyPart = domainModel.idBodyPart,
            name = domainModel.name
        )
    }
}