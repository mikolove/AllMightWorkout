package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.allmightworkout.business.domain.model.HistoryExercise
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.HistoryExerciseWithSetsCacheEntity
import javax.inject.Inject

class HistoryExerciseWithSetsCacheMapper
@Inject
constructor(
    private val historyExerciseCacheMapper: HistoryExerciseCacheMapper,
    private val historyExerciseSetCacheMapper: HistoryExerciseSetCacheMapper
) : EntityMapper<HistoryExerciseWithSetsCacheEntity, HistoryExercise>{

    override fun mapFromEntity(entity: HistoryExerciseWithSetsCacheEntity): HistoryExercise {

        var historyExercise = historyExerciseCacheMapper.mapFromEntity(entity.historyExerciseCacheEntity)
        var listOfHistoryExerciseSets = historyExerciseSetCacheMapper.entityListToDomainList(entity.listOfHistoryExerciseSetsCacheEntity)

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