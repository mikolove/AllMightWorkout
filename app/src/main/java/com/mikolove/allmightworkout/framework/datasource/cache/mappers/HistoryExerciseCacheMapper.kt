package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.allmightworkout.business.domain.model.HistoryExercise
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.HistoryExerciseCacheEntity
import com.mikolove.allmightworkout.framework.datasource.cache.util.RoomDateUtil
import javax.inject.Inject

class HistoryExerciseCacheMapper
@Inject
constructor(
    private val roomDateUtil: RoomDateUtil
) : EntityMapper<HistoryExerciseCacheEntity, HistoryExercise>{

    override fun mapFromEntity(entity: HistoryExerciseCacheEntity): HistoryExercise {
        return HistoryExercise(
            idHistoryExercise = entity.idHistoryExercise,
            name = entity.name,
            bodyPart = entity.bodyPart,
            workoutType = entity.workoutType,
            exerciseType = entity.exerciseType,
            historySets = listOf(),
            started_at = roomDateUtil.convertDateToStringDate(entity.started_at),
            ended_at = roomDateUtil.convertDateToStringDate(entity.ended_at),
            created_at = roomDateUtil.convertDateToStringDate(entity.created_at),
            updated_at = roomDateUtil.convertDateToStringDate(entity.updated_at)
        )
    }

    override fun mapToEntity(domainModel: HistoryExercise): HistoryExerciseCacheEntity {
        return HistoryExerciseCacheEntity(
            idHistoryExercise = domainModel.idHistoryExercise,
            idHistoryWorkout = null,
            name = domainModel.name,
            bodyPart = domainModel.bodyPart,
            workoutType = domainModel.workoutType,
            exerciseType = domainModel.exerciseType,
            started_at = roomDateUtil.convertStringDateToDate(domainModel.started_at),
            ended_at = roomDateUtil.convertStringDateToDate(domainModel.ended_at),
            created_at = roomDateUtil.convertStringDateToDate(domainModel.created_at),
            updated_at = roomDateUtil.convertStringDateToDate(domainModel.updated_at)

        )
    }

    override fun entityListToDomainList(entities: List<HistoryExerciseCacheEntity>): List<HistoryExercise> {
        val list : ArrayList<HistoryExercise> = ArrayList()
        for(entity in entities){
            list.add(mapFromEntity(entity))
        }
        return list
    }

    override fun domainListToEntityList(domains: List<HistoryExercise>): List<HistoryExerciseCacheEntity> {
        val entities : ArrayList<HistoryExerciseCacheEntity> = ArrayList()
        for(historyExercise in domains){
            entities.add(mapToEntity(historyExercise))
        }
        return entities
    }
}