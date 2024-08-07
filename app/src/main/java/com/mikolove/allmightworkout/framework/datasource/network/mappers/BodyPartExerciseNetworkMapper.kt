package com.mikolove.allmightworkout.framework.datasource.network.mappers

import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.core.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.BodyPartExerciseNetworkEntity

class BodyPartExerciseNetworkMapper : EntityMapper<BodyPartExerciseNetworkEntity, BodyPart> {

    override fun mapFromEntity(entity: BodyPartExerciseNetworkEntity): BodyPart {
        return BodyPart(
            idBodyPart = entity.idBodyPart,
            name = entity.name
        )
    }

    override fun mapToEntity(domainModel: BodyPart): BodyPartExerciseNetworkEntity {
        return BodyPartExerciseNetworkEntity(
            idBodyPart = domainModel.idBodyPart,
            name = domainModel.name
        )
    }
}