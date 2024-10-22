package com.mikolove.core.network.firebase.mappers

import com.mikolove.core.domain.workout.Group
import com.mikolove.core.network.firebase.model.GroupNetworkEntity
import com.mikolove.core.network.util.toFirebaseTimestamp
import com.mikolove.core.network.util.toZoneDateTime

fun GroupNetworkEntity.toGroup() : Group{
    return Group(
        idGroup = idWorkoutGroup,
        name = name,
        workouts = listOf(),
        createdAt = createdAt.toZoneDateTime(),
        updatedAt = updatedAt.toZoneDateTime()
    )
}

fun Group.toGroupNetworkEntity() : GroupNetworkEntity{
    return GroupNetworkEntity(
        idWorkoutGroup = idGroup,
        name = name,
        createdAt = createdAt.toFirebaseTimestamp(),
        updatedAt = updatedAt.toFirebaseTimestamp())
}