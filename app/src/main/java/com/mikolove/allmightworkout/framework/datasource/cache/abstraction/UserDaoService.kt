package com.mikolove.allmightworkout.framework.datasource.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.User

interface UserDaoService {

    suspend fun insertUser(user : User) : Long

    suspend fun updateName(name : String,primaryKey: String) : Int

    suspend fun getUser(primaryKey : String) : User?
}