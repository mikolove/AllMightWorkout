package com.mikolove.core.database.mappers

import com.mikolove.core.database.model.GroupCacheEntity
import com.mikolove.core.database.model.GroupsWithWorkoutsCacheEntity
import com.mikolove.core.domain.workout.Group

fun GroupsWithWorkoutsCacheEntity.toGroup() : Group{
    return Group(
        idGroup = groupCacheEntity.idGroup,
        name = groupCacheEntity.name,
        workouts = listOfWorkoutsCacheEntity?.map{ it.toWorkout()} ?: listOf(),
        createdAt = groupCacheEntity.createdAt,
        updatedAt = groupCacheEntity.updatedAt
    )
}

fun GroupCacheEntity.toGroup() : Group{
    return Group(
        idGroup = idGroup,
        name = name,
        workouts = listOf(),
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Group.toGroupCacheEntity(idUser : String) : GroupCacheEntity {
    return GroupCacheEntity(
        idGroup = idGroup,
        name = name,
        idUser = idUser,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}