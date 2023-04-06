package com.mikolove.allmightworkout.framework.datasource.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.User

interface UserFirestoreService {

    suspend fun insertUser(user :User)

    suspend fun updateName(name : String,primaryKey : String)

    suspend fun getUser(primaryKey: String) : User?

    suspend fun getUserWithWorkouts(primaryKey: String) : User?
}