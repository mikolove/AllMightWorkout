package com.mikolove.core.database.mappers

import com.mikolove.core.domain.analytics.HistoryExercise
import com.mikolove.core.domain.analytics.HistoryWorkout
import com.mikolove.core.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.HistoryWorkoutWithExercisesCacheEntity

class HistoryWorkoutWithExercisesCacheMapper
constructor(
    private val historyWorkoutCacheMapper: HistoryWorkoutCacheMapper,
    private val historyExerciseWithSetsCacheMapper: HistoryExerciseWithSetsCacheMapper
) : EntityMapper<HistoryWorkoutWithExercisesCacheEntity, HistoryWorkout> {

    override fun mapFromEntity(entity: HistoryWorkoutWithExercisesCacheEntity): HistoryWorkout {
        val historyWorkout = historyWorkoutCacheMapper.mapFromEntity(entity.historyWorkoutCacheEntity)
        val historyExercises : List<HistoryExercise>? = if(!entity.listOfHistoryExercisesCacheEntity.isNullOrEmpty()){
            entity.listOfHistoryExercisesCacheEntity.let {
                historyExerciseWithSetsCacheMapper.entityListToDomainList(it)
            }
        }else{
            null
        }


        historyWorkout.historyExercises = historyExercises
        return historyWorkout
    }

    override fun mapToEntity(domainModel: HistoryWorkout): HistoryWorkoutWithExercisesCacheEntity {
        TODO("Not yet implemented")
    }
}