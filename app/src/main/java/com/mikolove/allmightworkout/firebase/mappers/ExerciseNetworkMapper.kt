package com.mikolove.allmightworkout.firebase.mappers

import com.mikolove.allmightworkout.firebase.model.ExerciseNetworkEntity
import com.mikolove.allmightworkout.util.toFirebaseTimestamp
import com.mikolove.allmightworkout.util.toZoneDateTime
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.ExerciseType
import com.mikolove.core.domain.util.EntityMapper

class ExerciseNetworkMapper
constructor(
    private val bodyPartExerciseNetworkMapper : BodyPartExerciseNetworkMapper,
    private val exerciseSetNetworkMapper: ExerciseSetNetworkMapper
) : EntityMapper<ExerciseNetworkEntity, Exercise> {

    override fun mapFromEntity(entity: ExerciseNetworkEntity): Exercise {

        val sets     = entity.sets?.let { exerciseSetNetworkMapper.entityListToDomainList(it) } ?: listOf()
        val bodyPart = entity.bodyPart?.let { bodyPartExerciseNetworkMapper.entityListToDomainList(it) } ?: listOf()

        return Exercise(
            idExercise = entity.idExercise,
            name = entity.name,
            sets = sets,
            bodyPart = bodyPart,
            exerciseType = ExerciseType.valueOf(entity.exerciseType),
            isActive = entity.isActive,
            startedAt = null,
            endedAt = null,
            createdAt = entity.createdAt.toZoneDateTime(),
            updatedAt = entity.updatedAt.toZoneDateTime()
        )
    }

    override fun mapToEntity(domainModel: Exercise): ExerciseNetworkEntity {

        val sets = domainModel.sets.let { exerciseSetNetworkMapper.domainListToEntityList(it) }

        val bodyPart = domainModel.bodyPart?.let { bodyPartExerciseNetworkMapper.domainListToEntityList(it) }

        return ExerciseNetworkEntity(
            idExercise = domainModel.idExercise,
            name = domainModel.name,
            bodyPart = bodyPart,
            sets = sets,
            exerciseType = domainModel.exerciseType.name,
            isActive = domainModel.isActive,
            createdAt = domainModel.createdAt.toFirebaseTimestamp(),
            updatedAt =domainModel.updatedAt.toFirebaseTimestamp())

    }
}