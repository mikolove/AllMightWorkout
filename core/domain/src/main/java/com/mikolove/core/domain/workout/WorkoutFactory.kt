package com.mikolove.core.domain.workout

import com.mikolove.core.domain.exercise.Exercise
import java.time.ZonedDateTime
import java.util.*

class WorkoutFactory{

    fun createWorkout(
        idWorkout : String = UUID.randomUUID().toString(),
        name : String,
        exercises : List<Exercise> = listOf(),
        isActive : Boolean = true,
        collection : List<Group> = listOf(),
    ) : Workout {
        val zdt = ZonedDateTime.now()
        return Workout(
            idWorkout = idWorkout,
            name = name,
            exercises = exercises,
            isActive = isActive,
            startedAt = null,
            endedAt = null,
            groups = collection,
            createdAt = zdt,
            updatedAt = zdt
        )
    }
}