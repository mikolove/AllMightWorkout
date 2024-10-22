package com.mikolove.core.network.firebase.mappers

import com.mikolove.allmightworkout.firebase.model.BodyPartExerciseNetworkEntity
import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.core.domain.util.EntityMapper

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