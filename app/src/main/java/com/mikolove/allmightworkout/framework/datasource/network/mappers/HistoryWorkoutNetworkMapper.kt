package com.mikolove.allmightworkout.framework.datasource.network.mappers

import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.HistoryWorkoutNetworkEntity

class HistoryWorkoutNetworkMapper
constructor(
    private val dateUtil: DateUtil,
    private val historyExerciseNetworkMapper : HistoryExerciseNetworkMapper
) : EntityMapper<HistoryWorkoutNetworkEntity,HistoryWorkout>{

    override fun mapFromEntity(entity: HistoryWorkoutNetworkEntity): HistoryWorkout {

        val historyExercises = entity.historyExercises?.let { historyExerciseNetworkMapper.entityListToDomainList(it) }
        return HistoryWorkout(
            idHistoryWorkout = entity.idHistoryWorkout,
            name = entity.name,
            historyExercises = historyExercises,
            startedAt = dateUtil.convertFirebaseTimestampToStringData(entity.startedAt),
            endedAt = dateUtil.convertFirebaseTimestampToStringData(entity.endedAt),
            createdAt = dateUtil.convertFirebaseTimestampToStringData(entity.createdAt),
            updatedAt = dateUtil.convertFirebaseTimestampToStringData(entity.updatedAt)
        )
    }

    override fun mapToEntity(domainModel: HistoryWorkout): HistoryWorkoutNetworkEntity {

        val historyExercises = domainModel.historyExercises?.let { historyExerciseNetworkMapper.domainListToEntityList(it) }
        return HistoryWorkoutNetworkEntity(
            idHistoryWorkout = domainModel.idHistoryWorkout,
            name = domainModel.name,
            historyExercises = historyExercises,
            startedAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.startedAt),
            endedAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.endedAt),
            createdAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.createdAt),
            updatedAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.updatedAt)
        )
    }
}