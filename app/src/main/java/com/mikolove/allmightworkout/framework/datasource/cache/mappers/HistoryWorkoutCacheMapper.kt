package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.HistoryWorkoutCacheEntity
import com.mikolove.allmightworkout.framework.datasource.cache.util.RoomDateUtil
import javax.inject.Inject

class HistoryWorkoutCacheMapper
@Inject
constructor(
    private val roomDateUtil: RoomDateUtil
) : EntityMapper<HistoryWorkoutCacheEntity, HistoryWorkout>{

    override fun mapFromEntity(entity: HistoryWorkoutCacheEntity): HistoryWorkout {
        return HistoryWorkout(
            idHistoryWorkout = entity.idHistoryWorkout,
            name = entity.name,
            historyExercises = listOf(),
            started_at = roomDateUtil.convertDateToStringDate(entity.started_at),
            ended_at = roomDateUtil.convertDateToStringDate(entity.ended_at),
            created_at = roomDateUtil.convertDateToStringDate(entity.created_at),
            updated_at = roomDateUtil.convertDateToStringDate(entity.updated_at)
        )
    }

    override fun mapToEntity(domainModel: HistoryWorkout): HistoryWorkoutCacheEntity {
        return HistoryWorkoutCacheEntity(
            idHistoryWorkout = domainModel.idHistoryWorkout,
            name = domainModel.name,
            started_at = roomDateUtil.convertStringDateToDate(domainModel.started_at),
            ended_at = roomDateUtil.convertStringDateToDate(domainModel.ended_at),
            created_at = roomDateUtil.convertStringDateToDate(domainModel.created_at),
            updated_at = roomDateUtil.convertStringDateToDate(domainModel.updated_at)
        )
    }

    override fun entityListToDomainList(entities: List<HistoryWorkoutCacheEntity>): List<HistoryWorkout> {
        val list : ArrayList<HistoryWorkout> = ArrayList()
        for(entity in entities){
            list.add(mapFromEntity(entity))
        }
        return list
    }

    override fun domainListToEntityList(domains: List<HistoryWorkout>): List<HistoryWorkoutCacheEntity> {
        val entities : ArrayList<HistoryWorkoutCacheEntity> = ArrayList()
        for(historyWorkout in domains){
            entities.add(mapToEntity(historyWorkout))
        }
        return entities
    }
}