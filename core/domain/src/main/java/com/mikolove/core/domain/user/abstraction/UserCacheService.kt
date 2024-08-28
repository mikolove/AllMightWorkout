package com.mikolove.core.domain.user.abstraction

import com.mikolove.core.domain.user.User

interface UserCacheService {

    suspend fun insertUser(user : User) : Long

    suspend fun updateName(name : String,primaryKey: String) : Int

    suspend fun getUser(primaryKey : String) : User?
}