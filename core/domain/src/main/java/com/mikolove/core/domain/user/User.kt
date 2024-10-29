package com.mikolove.core.domain.user

import com.mikolove.core.domain.workout.Workout
import java.time.ZonedDateTime

data class User(
    var idUser : String,
    var name : String? = null,
    var email : String? = null,
    var workouts : List<Workout> = listOf(),
    var createdAt: ZonedDateTime,
    var updatedAt : ZonedDateTime
) {
    companion object {
        fun create(
            idUser: String,
            email: String? = null,
            name: String?
        ): User {
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
