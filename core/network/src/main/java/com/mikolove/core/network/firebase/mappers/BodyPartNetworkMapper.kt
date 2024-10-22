package com.mikolove.core.network.firebase.mappers

import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.core.network.firebase.model.BodyPartNetworkEntity


fun BodyPartNetworkEntity.toBodyPart() : BodyPart{
    return BodyPart(
        idBodyPart = this.idBodyPart,
        name = this.name
    )
}

fun BodyPart.toBodyPartNetworkEntity() : BodyPartNetworkEntity{
    return BodyPartNetworkEntity(
        idBodyPart = this.idBodyPart,
        name = this.name
    )
}
