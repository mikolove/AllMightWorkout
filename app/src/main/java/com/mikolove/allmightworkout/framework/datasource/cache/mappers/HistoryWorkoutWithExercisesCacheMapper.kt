package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.allmightworkout.business.domain.model.HistoryExercise
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
        val historyExercises : List<HistoryExercise>?
        if(!entity.listOfHistoryExercisesCacheEntity.isNullOrEmpty()){
            historyExercises = entity.listOfHistoryExercisesCacheEntity?.let {
                historyExerciseWithSetsCacheMapper.entityListToDomainList(it)
                }
        }else{
            historyExercises = null
        }


        historyWorkout.historyExercises = historyExercises
        return historyWorkout
    }

    override fun mapToEntity(domainModel: HistoryWorkout): HistoryWorkoutWithExercisesCacheEntity {
        TODO("Not yet implemented")
    }
}