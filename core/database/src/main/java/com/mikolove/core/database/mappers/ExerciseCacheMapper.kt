package com.mikolove.core.database.mappers

import com.mikolove.allmightworkout.framework.datasource.cache.model.ExerciseCacheEntity
import com.mikolove.core.database.model.ExerciseCacheEntity
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.ExerciseType
import com.mikolove.core.domain.util.EntityMapper
import javax.inject.Inject

class ExerciseCacheMapper
@Inject
constructor(
) : EntityMapper<ExerciseCacheEntity, Exercise> {

    override fun mapFromEntity(entity: ExerciseCacheEntity): Exercise {
        return Exercise(
            idExercise = entity.idExercise,
            name = entity.name,
            sets = listOf(),
            bodyPart = listOf(),
            exerciseType = ExerciseType.valueOf(entity.exerciseType),
            isActive = entity.isActive,
            startedAt = null,
            endedAt = null,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    override fun mapToEntity(domainModel: Exercise): ExerciseCacheEntity {
        return ExerciseCacheEntity(
            idExercise = domainModel.idExercise,
            name = domainModel.name,
            idBodyPart = domainModel.bodyPart?.idBodyPart,
            exerciseType = domainModel.exerciseType.name,
            isActive = domainModel.isActive,
            createdAt = domainModel.createdAt,
            updatedAt = domainModel.updatedAt
        )
    }

}