package com.mikolove.allmightworkout.business.data.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.User

interface UserCacheDataSource {

    suspend fun insertUser(user : User) : Long

    suspend fun updateName(name : String,primaryKey: String) : Int

    suspend fun getUser(primaryKey : String) : User?
}