package com.mikolove.allmightworkout.framework.datasource.network.mappers

import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.ExerciseType
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.core.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.ExerciseNetworkEntity

class ExerciseNetworkMapper
constructor(
    private val dateUtil: DateUtil,
    private val bodyPartExerciseNetworkMapper : BodyPartExerciseNetworkMapper,
    private val exerciseSetNetworkMapper: ExerciseSetNetworkMapper
) : EntityMapper<ExerciseNetworkEntity, Exercise> {

    override fun mapFromEntity(entity: ExerciseNetworkEntity): Exercise {

        val sets     = entity.sets?.let { exerciseSetNetworkMapper.entityListToDomainList(it) } ?: listOf()
        val bodyPart = entity.bodyPart?.let { bodyPartExerciseNetworkMapper.mapFromEntity(it) }

        return Exercise(
            idExercise = entity.idExercise,
            name = entity.name,
            sets = sets,
            bodyPart = bodyPart,
            exerciseType = ExerciseType.valueOf(entity.exerciseType),
            isActive = entity.isActive,
            startedAt = null,
            endedAt = null,
            createdAt = dateUtil.convertFirebaseTimestampToStringData(entity.createdAt),
            updatedAt = dateUtil.convertFirebaseTimestampToStringData(entity.updatedAt)
        )
    }

    override fun mapToEntity(domainModel: Exercise): ExerciseNetworkEntity {

        val sets = domainModel.sets.let { exerciseSetNetworkMapper.domainListToEntityList(it) }
        val bodyPart = domainModel.bodyPart?.let { bodyPartExerciseNetworkMapper.mapToEntity(it) }
        return ExerciseNetworkEntity(
            idExercise = domainModel.idExercise,
            name = domainModel.name,
            bodyPart = bodyPart,
            sets = sets,
            exerciseType = domainModel.exerciseType.name,
            isActive = domainModel.isActive,
            createdAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.createdAt),
            updatedAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.updatedAt)
        )

    }
}