package com.mikolove.core.domain.user

import com.mikolove.core.domain.workout.Workout
import java.time.ZonedDateTime


data class User(
    var idUser : String,
    var name : String? = null,
    var email : String? = null,
    var workouts : List<Workout>? = null,
    var createdAt: ZonedDateTime,
    var updatedAt : ZonedDateTime
){

}
