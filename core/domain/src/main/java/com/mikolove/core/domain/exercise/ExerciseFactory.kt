package com.mikolove.core.domain.exercise

import com.mikolove.core.domain.util.DateUtil
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class ExerciseFactory
@Inject
constructor(private val dateUtil: DateUtil){

    fun createExercise(
        idExercise: String?,
        name: String?,
        sets: List<ExerciseSet>?,
        bodyPart: BodyPart?,
        exerciseType: ExerciseType,
        isActive: Boolean? = true,
        created_at: String?
    ) : Exercise {
        return Exercise(
            idExercise = idExercise ?: UUID.randomUUID().toString(),
            name = name ?: "New exercise",
            sets =  sets ?: ArrayList(),
            bodyPart = bodyPart,
            exerciseType = exerciseType,
            isActive = isActive ?: true,
            startedAt = null,
            endedAt = null,
            createdAt = created_at ?: dateUtil.getCurrentTimestamp(),
            updatedAt = dateUtil.getCurrentTimestamp()
        )
    }
}