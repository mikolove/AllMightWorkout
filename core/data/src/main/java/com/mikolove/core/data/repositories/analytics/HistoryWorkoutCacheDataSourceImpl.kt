package com.mikolove.core.data.repositories.analytics

import com.mikolove.core.domain.analytics.HistoryWorkout
import com.mikolove.core.domain.analytics.abstraction.HistoryWorkoutCacheDataSource
import com.mikolove.core.domain.analytics.abstraction.HistoryWorkoutCacheService

class HistoryWorkoutCacheDataSourceImpl
constructor(private val historyWorkoutCacheService : HistoryWorkoutCacheService)
    : HistoryWorkoutCacheDataSource {

    override suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout, idUser : String): Long = historyWorkoutCacheService.insertHistoryWorkout(historyWorkout,idUser)

    override suspend fun deleteHistoryWorkout(idHistoryWorkout: String): Int = historyWorkoutCacheService.deleteHistoryWorkout(idHistoryWorkout)

    override suspend fun getHistoryWorkouts(idUser : String): List<HistoryWorkout> = historyWorkoutCacheService.getHistoryWorkouts(idUser)

    override suspend fun getHistoryWorkoutById(historyWorkoutId: String): HistoryWorkout? = historyWorkoutCacheService.getHistoryWorkoutById(historyWorkoutId)

}
