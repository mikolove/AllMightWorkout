package com.mikolove.core.database.implementation

import com.mikolove.core.domain.user.User
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.UserDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.database.UserDao
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.UserCacheMapper

class UserDaoServiceImpl
constructor(
    private val userDao : UserDao,
    private val userCacheMapper : UserCacheMapper,
) : UserDaoService{

    override suspend fun insertUser(user: User): Long {
        return userDao.insertUser(userCacheMapper.mapToEntity(user))
    }

    override suspend fun updateName(name: String, primaryKey: String): Int {
        return userDao.updateName(name,primaryKey)
    }

    override suspend fun getUser(primaryKey: String): User? {
        return userDao.getUser(primaryKey)?.let {
            userCacheMapper.mapFromEntity(it)
        }
    }
}