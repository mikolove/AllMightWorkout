package com.mikolove.allmightworkout.framework.datasource.network.mappers

import com.mikolove.allmightworkout.business.domain.model.HistoryExerciseSet
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.HistoryExerciseSetNetworkEntity

class HistoryExerciseSetNetworkMapper
constructor(
    private val dateUtil: DateUtil
) : EntityMapper<HistoryExerciseSetNetworkEntity, HistoryExerciseSet>{

    override fun mapFromEntity(entity: HistoryExerciseSetNetworkEntity): HistoryExerciseSet {
        return HistoryExerciseSet(
            idHistoryExerciseSet = entity.idHistoryExerciseSet,
            reps = entity.reps,
            weight = entity.weight,
            time = entity.time,
            restTime = entity.restTime,
            startedAt = dateUtil.convertFirebaseTimestampToStringData(entity.startedAt),
            endedAt = dateUtil.convertFirebaseTimestampToStringData(entity.endedAt),
            createdAt = dateUtil.convertFirebaseTimestampToStringData(entity.createdAt),
            updatedAt = dateUtil.convertFirebaseTimestampToStringData(entity.updatedAt)
        )
    }

    override fun mapToEntity(domainModel: HistoryExerciseSet): HistoryExerciseSetNetworkEntity {
        return HistoryExerciseSetNetworkEntity(
            idHistoryExerciseSet = domainModel.idHistoryExerciseSet,
            reps = domainModel.reps,
            weight = domainModel.weight,
            time = domainModel.time,
            restTime = domainModel.restTime,
            startedAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.startedAt),
            endedAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.endedAt),
            createdAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.createdAt),
            updatedAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.updatedAt)
        )
    }
}