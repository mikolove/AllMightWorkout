package com.mikolove.core.domain.workout

import java.time.ZonedDateTime
import java.util.UUID

data class Group (
    var idGroup : String = UUID.randomUUID().toString(),
    var name : String,
    var workouts : List<Workout> = listOf(),
)