package com.mikolove.core.database.mappers

import com.mikolove.core.database.model.BodyPartCacheEntity
import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.core.domain.util.EntityMapper

class BodyPartCacheMapper  : EntityMapper<BodyPartCacheEntity, BodyPart> {

    override fun mapFromEntity(entity: BodyPartCacheEntity): BodyPart {
        return BodyPart(
            idBodyPart = entity.idBodyPart,
            name = entity.name
        )
    }

    override fun mapToEntity(domainModel: BodyPart): BodyPartCacheEntity {
        return BodyPartCacheEntity(
            idBodyPart = domainModel.idBodyPart,
            name = domainModel.name,
            idWorkoutType = null
        )
    }

}