package com.mikolove.core.network.mappers

import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.core.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.ExerciseSetNetworkEntity

class ExerciseSetNetworkMapper
constructor(
    private val dateUtil: DateUtil
) : EntityMapper<ExerciseSetNetworkEntity, ExerciseSet> {

    override fun mapFromEntity(entity: ExerciseSetNetworkEntity): ExerciseSet {
        return ExerciseSet(
            idExerciseSet = entity.idExerciseSet,
            reps = entity.reps,
            weight = entity.weight,
            time = entity.time,
            restTime = entity.restTime,
            order = entity.order,
            startedAt = null,
            endedAt = null,
            createdAt = dateUtil.convertFirebaseTimestampToStringData(entity.createdAt),
            updatedAt = dateUtil.convertFirebaseTimestampToStringData(entity.updatedAt)
        )
    }

    override fun mapToEntity(domainModel: ExerciseSet): ExerciseSetNetworkEntity {
        return ExerciseSetNetworkEntity(
            idExerciseSet = domainModel.idExerciseSet,
            reps = domainModel.reps,
            weight = domainModel.weight,
            time = domainModel.time,
            restTime = domainModel.restTime,
            order = domainModel.order,
            createdAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.createdAt),
            updatedAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.updatedAt)
        )
    }
}