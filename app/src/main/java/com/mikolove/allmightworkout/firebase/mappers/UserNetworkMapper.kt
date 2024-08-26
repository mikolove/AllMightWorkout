package com.mikolove.allmightworkout.firebase.mappers

import com.mikolove.allmightworkout.firebase.model.UserNetworkEntity
import com.mikolove.allmightworkout.util.toFirebaseTimestamp
import com.mikolove.allmightworkout.util.toZoneDateTime
import com.mikolove.core.domain.user.User
import com.mikolove.core.domain.util.EntityMapper

class UserNetworkMapper : EntityMapper<UserNetworkEntity, User> {

    override fun mapFromEntity(entity: UserNetworkEntity): User {
        return User(
            idUser = entity.idUser,
            name = entity.name,
            email = entity.email,
            workouts = listOf(),
            createdAt = entity.createdAt.toZoneDateTime(),
            updatedAt = entity.updatedAt.toZoneDateTime()
        )
    }

    override fun mapToEntity(domainModel: User): UserNetworkEntity {
        return UserNetworkEntity(
            idUser = domainModel.idUser,
            name = domainModel.name,
            email = domainModel.email,
            createdAt = domainModel.createdAt.toFirebaseTimestamp(),
            updatedAt = domainModel.updatedAt.toFirebaseTimestamp()
        )
    }
}