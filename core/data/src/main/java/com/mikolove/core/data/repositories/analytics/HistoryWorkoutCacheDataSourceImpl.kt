package com.mikolove.core.data.repositories.analytics

import com.mikolove.core.domain.analytics.abstraction.HistoryWorkoutCacheDataSource
import com.mikolove.core.domain.analytics.abstraction.HistoryWorkoutCacheService
import com.mikolove.core.domain.analytics.HistoryWorkout

class HistoryWorkoutCacheDataSourceImpl
constructor(private val historyWorkoutCacheService : HistoryWorkoutCacheService)
    : HistoryWorkoutCacheDataSource {

    override suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout, idUser : String): Long = historyWorkoutCacheService.insertHistoryWorkout(historyWorkout,idUser)

    override suspend fun deleteHistoryWorkout(idHistoryWorkout: String): Int = historyWorkoutCacheService.deleteHistoryWorkout(idHistoryWorkout)

    override suspend fun getHistoryWorkouts(
        query: String,
        filterAndOrder: String,
        page: Int,
        idUser : String
    ): List<HistoryWorkout> {

        return historyWorkoutCacheService.returnOrderedQuery(query, filterAndOrder, page, idUser)
    }

    override suspend fun getHistoryWorkoutById(historyWorkoutId: String): HistoryWorkout? = historyWorkoutCacheService.getHistoryWorkoutById(historyWorkoutId)

    override suspend fun getTotalHistoryWorkout(idUser : String): Int = historyWorkoutCacheService.getTotalHistoryWorkout(idUser)
}
