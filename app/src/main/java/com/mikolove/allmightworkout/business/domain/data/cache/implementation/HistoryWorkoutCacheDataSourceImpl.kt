package com.mikolove.allmightworkout.business.domain.data.cache.implementation

import com.mikolove.allmightworkout.business.domain.data.cache.abstraction.HistoryWorkoutCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import javax.inject.Inject
import javax.inject.Singleton

/*
@Singleton
class HistoryWorkoutCacheDataSourceImpl
@Inject
constructor(private val historyWorkoutDaoService : HistoryWorkoutDaoService)
    : HistoryWorkoutCacheDataSource {

    override suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout): Long = historyWorkoutDaoService.insertHistoryWorkout(historyWorkout)

    override suspend fun updateHistoryWorkout(historyWorkout: HistoryWorkout): Int = historyWorkoutDaoService.updateHistoryWorkout(historyWorkout)

    override suspend fun getHistoryWorkout(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<HistoryWorkout> = historyWorkoutDaoService.getHistoryWorkout(query,filterAndOrder,page)

    override suspend fun getHistoryWorkoutById(primareyKey: Long): HistoryWorkout? = historyWorkoutDaoService.getHistoryWorkoutById(primareyKey)
}*/
