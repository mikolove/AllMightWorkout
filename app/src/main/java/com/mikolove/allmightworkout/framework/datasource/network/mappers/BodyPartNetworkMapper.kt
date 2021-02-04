package com.mikolove.allmightworkout.framework.datasource.network.mappers

import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.BodyPartNetworkEntity

class BodyPartNetworkMapper : EntityMapper<BodyPartNetworkEntity,BodyPart>{

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