package com.mikolove.allmightworkout.business.domain.model

import com.mikolove.allmightworkout.business.domain.util.DateUtil
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserFactory
@Inject
constructor(private val dateUtil: DateUtil){

    fun createUser(
        idUser : String,
        email : String,
        name : String?
    ) : User{
        return User(
            idUser = idUser,
            name = name ,
            email = email,
            createdAt = dateUtil.getCurrentTimestamp(),
            updatedAt = dateUtil.getCurrentTimestamp()
        )
    }
}