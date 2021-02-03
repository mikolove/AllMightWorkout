package com.mikolove.allmightworkout.framework.datasource.network.mappers

import com.mikolove.allmightworkout.business.domain.model.HistoryExercise
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.HistoryExerciseNetworkEntity

class HistoryExerciseNetworkMapper
constructor(
    private val dateUtil: DateUtil,
    private val historyExerciseSetNetworkMapper : HistoryExerciseSetNetworkMapper
) : EntityMapper<HistoryExerciseNetworkEntity,HistoryExercise>{

    override fun mapFromEntity(entity: HistoryExerciseNetworkEntity): HistoryExercise {

        val sets = entity.historySets?.let { historyExerciseSetNetworkMapper.entityListToDomainList(it) }
        return HistoryExercise(
            idHistoryExercise = entity.idHistoryExercise,
            name = entity.name,
            bodyPart = entity.bodyPart,
            workoutType = entity.workoutType,
            exerciseType = entity.exerciseType,
            historySets = sets,
            startedAt = dateUtil.convertFirebaseTimestampToStringData(entity.startedAt),
            endedAt = dateUtil.convertFirebaseTimestampToStringData(entity.endedAt),
            createdAt = dateUtil.convertFirebaseTimestampToStringData(entity.createdAt),
            updatedAt = dateUtil.convertFirebaseTimestampToStringData(entity.updatedAt)
        )
    }

    override fun mapToEntity(domainModel: HistoryExercise): HistoryExerciseNetworkEntity {

        val sets = domainModel.historySets?.let { historyExerciseSetNetworkMapper.domainListToEntityList(it) }
        return HistoryExerciseNetworkEntity(
            idHistoryExercise = domainModel.idHistoryExercise,
            name = domainModel.name,
            bodyPart = domainModel.bodyPart,
            workoutType = domainModel.workoutType,
            exerciseType = domainModel.exerciseType,
            historySets = sets,
            startedAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.startedAt),
            endedAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.endedAt),
            createdAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.createdAt),
            updatedAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.updatedAt)
        )
    }
}