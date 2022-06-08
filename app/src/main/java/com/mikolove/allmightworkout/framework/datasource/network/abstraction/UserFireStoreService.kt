package com.mikolove.allmightworkout.framework.datasource.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.User

interface UserFireStoreService {

    suspend fun insertUser(user :User)

    suspend fun updateName(name : String,primaryKey : String)

    suspend fun getUserWithWorkouts(primaryKey: String) : User?
}