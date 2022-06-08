package com.mikolove.allmightworkout.framework.datasource.network.mappers

import com.mikolove.allmightworkout.business.domain.model.User
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.UserNetworkEntity

class UserNetworkMapper
constructor(private val dateUtil: DateUtil) : EntityMapper<UserNetworkEntity, User>{

    override fun mapFromEntity(entity: UserNetworkEntity): User {
        return User(
            idUser = entity.idUser,
            name = entity.name,
            email = entity.email,
            workouts = listOf(),
            createdAt = dateUtil.convertFirebaseTimestampToStringData(entity.createdAt),
            updatedAt = dateUtil.convertFirebaseTimestampToStringData(entity.updatedAt)
        )
    }

    override fun mapToEntity(domainModel: User): UserNetworkEntity {

    }
}