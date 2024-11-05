package com.mikolove.core.database.mappers

import com.mikolove.core.database.model.WorkoutTypeCacheEntity
import com.mikolove.core.database.model.WorkoutTypeWithBodyPartCacheEntity
import com.mikolove.core.domain.workouttype.WorkoutType

fun WorkoutTypeWithBodyPartCacheEntity.toWorkoutType() : WorkoutType{
    return WorkoutType(
        idWorkoutType = workoutTypeCacheEntity.idWorkoutType,
        name = workoutTypeCacheEntity.name,
        bodyParts = listOfBodyPartCacheEntity.map { it.toBodyPart() }
    )
}

fun WorkoutType.toWorkoutTypeCacheEntity() : WorkoutTypeCacheEntity{
    return WorkoutTypeCacheEntity(
        idWorkoutType = idWorkoutType,
        name = name
    )
}

fun WorkoutType.toWorkoutTypeWithBodyPartCacheEntity() : WorkoutTypeWithBodyPartCacheEntity{
    return WorkoutTypeWithBodyPartCacheEntity(
        workoutTypeCacheEntity = this.toWorkoutTypeCacheEntity(),
        listOfBodyPartCacheEntity = this.bodyParts.map { it.toBodyPartCacheEntity(this.idWorkoutType) }
    )
}