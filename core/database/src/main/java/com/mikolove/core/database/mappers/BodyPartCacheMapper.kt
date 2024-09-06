package com.mikolove.core.database.mappers

import com.mikolove.core.database.model.BodyPartCacheEntity
import com.mikolove.core.domain.bodypart.BodyPart

fun BodyPartCacheEntity.toBodyPart() : BodyPart{
    return BodyPart(
        idBodyPart = idBodyPart,
        name =name
    )
}

fun BodyPart.toBodyPartCacheEntity(idWorkoutType : String) : BodyPartCacheEntity{
    return BodyPartCacheEntity(
        idBodyPart = idBodyPart,
        name = name,
        idWorkoutType = idWorkoutType
    )
}