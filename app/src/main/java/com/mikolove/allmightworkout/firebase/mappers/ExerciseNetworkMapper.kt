package com.mikolove.allmightworkout.firebase.mappers

import com.mikolove.allmightworkout.firebase.model.ExerciseNetworkEntity
import com.mikolove.allmightworkout.util.toFirebaseTimestamp
import com.mikolove.allmightworkout.util.toZoneDateTime
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.ExerciseType
import com.mikolove.core.domain.util.EntityMapper

class ExerciseNetworkMapper
constructor(
) : EntityMapper<ExerciseNetworkEntity, Exercise> {

    override fun mapFromEntity(entity: ExerciseNetworkEntity): Exercise {

        return Exercise(
            idExercise = entity.idExercise,
            name = entity.name,
            exerciseType = ExerciseType.valueOf(entity.exerciseType),
            isActive = entity.isActive,
            startedAt = null,
            endedAt = null,
            createdAt = entity.createdAt.toZoneDateTime(),
            updatedAt = entity.updatedAt.toZoneDateTime()
        )
    }

    override fun mapToEntity(domainModel: Exercise): ExerciseNetworkEntity {

        return ExerciseNetworkEntity(
            idExercise = domainModel.idExercise,
            name = domainModel.name,
            bodyPartIds = listOf(),
            exerciseType = domainModel.exerciseType.name,
            isActive = domainModel.isActive,
            createdAt = domainModel.createdAt.toFirebaseTimestamp(),
            updatedAt =domainModel.updatedAt.toFirebaseTimestamp())
    }
}