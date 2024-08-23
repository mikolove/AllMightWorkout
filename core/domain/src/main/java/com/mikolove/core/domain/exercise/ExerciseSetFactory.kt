package com.mikolove.core.domain.exercise

import java.time.ZonedDateTime
import java.util.UUID

class ExerciseSetFactory{

    fun createExerciseSet(
        idExerciseSet : String = UUID.randomUUID().toString() ,
        reps : Int = 8,
        weight : Int = 8,
        time : Int = 45,
        restTime : Int = 45
    ) : ExerciseSet {

        val zdt = ZonedDateTime.now()
        return ExerciseSet(
            idExerciseSet = idExerciseSet ?: UUID.randomUUID().toString(),
            reps = reps ,
            weight = weight ,
            time = time ,
            restTime = restTime ,
            order = 0,
            startedAt = null,
            endedAt = null,
            createdAt = zdt,
            updatedAt = zdt)
    }

    fun createExerciseSet(
        idExerciseSet : String = UUID.randomUUID().toString() ,
        reps : Int = 8,
        weight : Int = 8,
        time : Int = 45,
        restTime : Int = 45,
        order : Int = 0,
    ) : ExerciseSet {
        val zdt = ZonedDateTime.now()
        return ExerciseSet(
            idExerciseSet = idExerciseSet ?: UUID.randomUUID().toString(),
            reps = reps,
            weight = weight,
            time = time,
            restTime = restTime,
            order = order ,
            startedAt = null,
            endedAt = null,
            createdAt = zdt,
            updatedAt = zdt)
    }

}