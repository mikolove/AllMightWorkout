package com.mikolove.allmightworkout.business.domain.model

import com.mikolove.core.domain.util.DateUtil
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserFactory
@Inject
constructor(private val dateUtil: DateUtil){

    fun createUser(
        idUser : String,
        email : String? = null,
        name : String?
    ) : User{
        return User(
            idUser = idUser,
            name = name,
            email = email,
            createdAt = dateUtil.getCurrentTimestamp(),
            updatedAt = dateUtil.getCurrentTimestamp()
        )
    }
}