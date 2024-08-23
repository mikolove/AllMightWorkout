package com.mikolove.allmightworkout.firebase.abstraction

import com.mikolove.core.domain.user.User

interface UserFirestoreService {

    suspend fun insertUser(user : User)

    suspend fun updateName(name : String,primaryKey : String)

    suspend fun getUser(primaryKey: String) : User?

    suspend fun getUserWithWorkouts(primaryKey: String) : User?
}