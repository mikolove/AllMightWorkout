package com.mikolove.core.data.analytics

import com.mikolove.core.domain.analytics.HistoryExerciseCacheDataSource
import com.mikolove.core.domain.analytics.HistoryExercise
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.HistoryExerciseDaoService

class HistoryExerciseCacheDataSourceImpl
constructor(private val historyExerciseDaoService : HistoryExerciseDaoService)
    : HistoryExerciseCacheDataSource {

    override suspend fun insertHistoryExercise(
        historyExercise: HistoryExercise,
        idHistoryWorkout: String
    ): Long = historyExerciseDaoService.insertHistoryExercise(
        historyExercise,
        idHistoryWorkout
    )

    override suspend fun getHistoryExercisesByHistoryWorkout(idHistoryWorkout: String): List<HistoryExercise>? = historyExerciseDaoService.getHistoryExercisesByHistoryWorkout(idHistoryWorkout)

    override suspend fun getHistoryExerciseById(primaryKey: String): HistoryExercise? = historyExerciseDaoService.getHistoryExerciseById(primaryKey)

    override suspend fun getTotalHistoryExercise(): Int = historyExerciseDaoService.getTotalHistoryExercise()
}
