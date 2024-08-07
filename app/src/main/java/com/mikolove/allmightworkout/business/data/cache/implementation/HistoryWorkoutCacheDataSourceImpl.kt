package com.mikolove.allmightworkout.business.data.cache.implementation

import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryWorkoutCacheDataSource
import com.mikolove.core.domain.analytics.HistoryWorkout
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.HistoryWorkoutDaoService

class HistoryWorkoutCacheDataSourceImpl
constructor(private val historyWorkoutDaoService : HistoryWorkoutDaoService)
    : HistoryWorkoutCacheDataSource {

    override suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout, idUser : String): Long = historyWorkoutDaoService.insertHistoryWorkout(historyWorkout,idUser)

    override suspend fun deleteHistoryWorkout(idHistoryWorkout: String): Int = historyWorkoutDaoService.deleteHistoryWorkout(idHistoryWorkout)

    override suspend fun getHistoryWorkouts(
        query: String,
        filterAndOrder: String,
        page: Int,
        idUser : String
    ): List<HistoryWorkout> {

        return historyWorkoutDaoService.returnOrderedQuery(query, filterAndOrder, page, idUser)
    }

    override suspend fun getHistoryWorkoutById(historyWorkoutId: String): HistoryWorkout? = historyWorkoutDaoService.getHistoryWorkoutById(historyWorkoutId)

    override suspend fun getTotalHistoryWorkout(idUser : String): Int = historyWorkoutDaoService.getTotalHistoryWorkout(idUser)
}
