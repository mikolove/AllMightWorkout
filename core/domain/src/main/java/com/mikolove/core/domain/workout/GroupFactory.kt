package com.mikolove.core.domain.workout

import java.time.ZonedDateTime
import java.util.UUID

class GroupFactory{

    fun createGroup(
        idGroup : String = UUID.randomUUID().toString(),
        name : String,
        workouts : List<Workout> = listOf(),
    ) : Group {
        val zdt = ZonedDateTime.now()
        return Group(
            idGroup = idGroup,
            name = name,
            workouts = workouts,
            createdAt = zdt,
            updatedAt = zdt
        )
    }
}