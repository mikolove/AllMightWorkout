package com.mikolove.core.database.mappers

import com.mikolove.core.domain.user.User
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.core.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.UserCacheEntity

class UserCacheMapper
constructor(
    private val dateUtil: DateUtil
)
    : EntityMapper<UserCacheEntity, User> {

    override fun mapFromEntity(entity: UserCacheEntity): User {
        return User(
            idUser = entity.idUser,
            name = entity.name,
            email = entity.email,
            createdAt = dateUtil.convertDateToStringDate(entity.createdAt),
            updatedAt = dateUtil.convertDateToStringDate(entity.updatedAt)
        )
    }

    override fun mapToEntity(domainModel: User): UserCacheEntity {
        return UserCacheEntity(
            idUser = domainModel.idUser,
            name = domainModel.name,
            email = domainModel.email,
            createdAt = dateUtil.convertStringDateToDate(domainModel.createdAt),
            updatedAt = dateUtil.convertStringDateToDate(domainModel.updatedAt)
        )
    }

}