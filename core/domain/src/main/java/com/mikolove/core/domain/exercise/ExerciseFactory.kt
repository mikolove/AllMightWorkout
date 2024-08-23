package com.mikolove.core.domain.exercise

import com.mikolove.core.domain.bodypart.BodyPart
import java.time.ZonedDateTime

import java.util.UUID


class ExerciseFactory {

    fun createExercise(
        idExercise: String = UUID.randomUUID().toString(),
        name: String,
        sets: List<ExerciseSet> = listOf(),
        bodyPart: List<BodyPart> = listOf(),
        exerciseType: ExerciseType,
        isActive: Boolean = true,
    ) : Exercise {
        val zdt = ZonedDateTime.now()
        return Exercise(
            idExercise = idExercise,
            name = name ,
            sets =  sets ,
            bodyPart = bodyPart,
            exerciseType = exerciseType,
            isActive = isActive,
            startedAt = null,
            endedAt = null,
            createdAt = zdt,
            updatedAt = zdt
        )
    }
}