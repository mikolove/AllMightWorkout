package com.mikolove.core.data.repositories.analytics

import com.mikolove.core.domain.analytics.abstraction.HistoryExerciseCacheDataSource
import com.mikolove.core.domain.analytics.abstraction.HistoryExerciseCacheService
import com.mikolove.core.domain.analytics.HistoryExercise

class HistoryExerciseCacheDataSourceImpl
constructor(private val historyExerciseCacheService : HistoryExerciseCacheService)
    : HistoryExerciseCacheDataSource {

    override suspend fun insertHistoryExercise(
        historyExercise: HistoryExercise,
        idHistoryWorkout: String
    ): Long = historyExerciseCacheService.insertHistoryExercise(
        historyExercise,
        idHistoryWorkout
    )

    override suspend fun getHistoryExercisesByHistoryWorkout(idHistoryWorkout: String): List<HistoryExercise>? = historyExerciseCacheService.getHistoryExercisesByHistoryWorkout(idHistoryWorkout)

    override suspend fun getHistoryExerciseById(primaryKey: String): HistoryExercise? = historyExerciseCacheService.getHistoryExerciseById(primaryKey)

}
