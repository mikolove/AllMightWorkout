package com.mikolove.allmightworkout.business.data.cache.implementation

import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryWorkoutCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.HistoryWorkoutDaoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryWorkoutCacheDataSourceImpl
@Inject
constructor(private val historyWorkoutDaoService : HistoryWorkoutDaoService)
    : HistoryWorkoutCacheDataSource {

    override suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout): Long = historyWorkoutDaoService.insertHistoryWorkout(historyWorkout)

    override suspend fun getLastHistoryWorkout(): HistoryWorkout? = historyWorkoutDaoService.getLastHistoryWorkout()

    override suspend fun getHistoryWorkouts(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<HistoryWorkout> {

        return historyWorkoutDaoService.returnOrderedQuery(query, filterAndOrder, page)
    }

    override suspend fun getHistoryWorkoutById(historyWorkoutId: String): HistoryWorkout? = historyWorkoutDaoService.getHistoryWorkoutById(historyWorkoutId)

    override suspend fun getTotalHistoryWorkout(): Int = historyWorkoutDaoService.getTotalHistoryWorkout()
}
