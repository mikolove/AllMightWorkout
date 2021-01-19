package com.mikolove.allmightworkout.business.domain.model

import com.mikolove.allmightworkout.business.domain.util.DateUtil
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class ExerciseFactory
@Inject
constructor( private val dateUtil: DateUtil,
             private val exerciseSetFactory: ExerciseSetFactory,
            private val bodyPartFactory: BodyPartFactory){

    fun createExercise(
        idExercise : String?,
        name : String?,
        sets : List<ExerciseSet>?,
        bodyPart: BodyPart,
        exerciseType : ExerciseType,
        isActive : Boolean? = true,
        created_at : String?
    ) : Exercise {
        return Exercise(
            idExercise = idExercise ?: UUID.randomUUID().toString(),
            name = name ?: "New exercise",
            sets =  sets ?: ArrayList(),
            bodyPart = bodyPart,
            exerciseType = exerciseType,
            isActive = isActive ?: true,
            started_at = null,
            ended_at = null,
            created_at = created_at ?: dateUtil.getCurrentTimestamp(),
            updated_at = dateUtil.getCurrentTimestamp()
        )
    }
}