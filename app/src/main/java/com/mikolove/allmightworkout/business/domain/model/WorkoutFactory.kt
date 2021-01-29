package com.mikolove.allmightworkout.business.domain.model

import com.mikolove.allmightworkout.business.domain.util.DateUtil
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutFactory
@Inject
constructor( private val dateUtil: DateUtil ,
             private val exerciseFactory: ExerciseFactory){

    fun createWorkout(
        idWorkout : String? ,
        name : String?,
        exercises : List<Exercise>?,
        isActive : Boolean? = true,
        created_at : String?
    ) : Workout {
        return Workout(
            idWorkout = idWorkout ?: UUID.randomUUID().toString(),
            name = name ?: "New workout",
            exercises = exercises ,
            isActive = isActive ?: true,
            startedAt = null,
            endedAt = null,
            createdAt = created_at?: dateUtil.getCurrentTimestamp(),
            updatedAt = dateUtil.getCurrentTimestamp()
        )
    }

}