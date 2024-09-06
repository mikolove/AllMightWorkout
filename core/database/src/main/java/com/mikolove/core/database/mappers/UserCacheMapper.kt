package com.mikolove.core.database.mappers

import com.mikolove.core.database.model.UserCacheEntity
import com.mikolove.core.database.model.UserWithWorkoutAndExerciseCacheEntity
import com.mikolove.core.domain.user.User

fun UserCacheEntity.toUser() : User{
    return User(
        idUser = idUser,
        name = name,
        email = email,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun User.toUserCacheEntity() : UserCacheEntity{
    return UserCacheEntity(
        idUser = idUser,
        name = name,
        email = email,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun UserWithWorkoutAndExerciseCacheEntity.toUser() : User{
    return User(
        idUser = userCacheEntity.idUser,
        name = userCacheEntity.name,
        email = userCacheEntity.email,
        workouts = listOfWorkoutCacheEntity?.map { it.toWorkout() } ?: listOf(),
        createdAt = userCacheEntity.createdAt,
        updatedAt = userCacheEntity.updatedAt
    )
}