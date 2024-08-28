package com.mikolove.core.database.mappers

import com.mikolove.core.domain.analytics.HistoryExercise
import com.mikolove.core.domain.analytics.HistoryExerciseSet
import com.mikolove.core.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.HistoryExerciseWithSetsCacheEntity

class HistoryExerciseWithSetsCacheMapper
constructor(
    private val historyExerciseCacheMapper: HistoryExerciseCacheMapper,
    private val historyExerciseSetCacheMapper: HistoryExerciseSetCacheMapper
) : EntityMapper<HistoryExerciseWithSetsCacheEntity, HistoryExercise> {

    override fun mapFromEntity(entity: HistoryExerciseWithSetsCacheEntity): HistoryExercise {

        val historyExercise = historyExerciseCacheMapper.mapFromEntity(entity.historyExerciseCacheEntity)
        val listOfHistoryExerciseSets : List<HistoryExerciseSet> = if(!entity.listOfHistoryExerciseSetsCacheEntity.isNullOrEmpty()) {
            entity.listOfHistoryExerciseSetsCacheEntity.let {
                historyExerciseSetCacheMapper.entityListToDomainList(it)
            }
        }else{
            listOf()
        }

        historyExercise.historySets = listOfHistoryExerciseSets
        return historyExercise

    }

    override fun mapToEntity(domainModel: HistoryExercise): HistoryExerciseWithSetsCacheEntity {
        TODO("Not yet implemented")
    }

    override fun entityListToDomainList(entities: List<HistoryExerciseWithSetsCacheEntity>): List<HistoryExercise> {
        val list : ArrayList<HistoryExercise> = ArrayList()
        for(entity in entities){
            list.add(mapFromEntity(entity))
        }
        return list
    }

    override fun domainListToEntityList(domains: List<HistoryExercise>): List<HistoryExerciseWithSetsCacheEntity> {
        TODO("Not yet implemented")
    }
}