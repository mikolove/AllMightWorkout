package com.mikolove.core.network.firebase.mappers

import com.mikolove.core.domain.user.User
import com.mikolove.core.network.firebase.model.UserNetworkEntity
import com.mikolove.core.network.util.toFirebaseTimestamp
import com.mikolove.core.network.util.toZoneDateTime

fun UserNetworkEntity.toUser() : User {
    return User(
        idUser = this.idUser,
        name = this.name,
        email = this.email,
        workouts = listOf(),
        createdAt = this.createdAt.toZoneDateTime(),
        updatedAt = this.updatedAt.toZoneDateTime()
    )
}

fun User.toUserNetworkEntity() : UserNetworkEntity {
    return UserNetworkEntity(
        idUser = this.idUser,
        name = this.name,
        email = this.email,
        createdAt = this.createdAt.toFirebaseTimestamp(),
        updatedAt = this.updatedAt.toFirebaseTimestamp()
    )
}