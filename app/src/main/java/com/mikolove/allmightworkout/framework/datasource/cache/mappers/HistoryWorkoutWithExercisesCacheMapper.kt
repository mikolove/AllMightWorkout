package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.HistoryWorkoutWithExercisesCacheEntity

class HistoryWorkoutWithExercisesCacheMapper
constructor(
    private val historyWorkoutCacheMapper: HistoryWorkoutCacheMapper,
    private val historyExerciseWithSetsCacheMapper: HistoryExerciseWithSetsCacheMapper
) : EntityMapper<HistoryWorkoutWithExercisesCacheEntity,HistoryWorkout>{

    override fun mapFromEntity(entity: HistoryWorkoutWithExercisesCacheEntity): HistoryWorkout {
        val historyWorkout = historyWorkoutCacheMapper.mapFromEntity(entity.historyWorkoutCacheEntity)
        val historyExercises = historyExerciseWithSetsCacheMapper.entityListToDomainList(entity.listOfHistoryExercisesCacheEntity)

        historyWorkout.historyExercises = historyExercises
        return historyWorkout
    }

    override fun mapToEntity(domainModel: HistoryWorkout): HistoryWorkoutWithExercisesCacheEntity {
        TODO("Not yet implemented")
    }

    override fun entityListToDomainList(entities: List<HistoryWorkoutWithExercisesCacheEntity>): List<HistoryWorkout> {
        TODO("Not yet implemented")
    }

    override fun domainListToEntityList(domains: List<HistoryWorkout>): List<HistoryWorkoutWithExercisesCacheEntity> {
        TODO("Not yet implemented")
    }
}