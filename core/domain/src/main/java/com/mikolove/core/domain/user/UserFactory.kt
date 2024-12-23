package com.mikolove.core.domain.user

import java.time.ZonedDateTime

class UserFactory{
    companion object{
        fun createUser(
            idUser : String,
            email : String? = null,
            name : String?
        ) : User {
            val zdt = ZonedDateTime.now()
            return User(
                idUser = idUser,
                name = name,
                email = email,
                createdAt = zdt,
                updatedAt = zdt
            )
        }
    }
}