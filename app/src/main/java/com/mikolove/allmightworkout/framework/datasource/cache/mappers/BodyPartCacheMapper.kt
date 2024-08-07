package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.core.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.BodyPartCacheEntity
import javax.inject.Inject

class BodyPartCacheMapper
@Inject
constructor() : EntityMapper<BodyPartCacheEntity, BodyPart> {

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