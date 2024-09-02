package com.mikolove.core.data.repositories.analytics

import com.mikolove.core.domain.analytics.abstraction.HistoryExerciseSetCacheDataSource
import com.mikolove.core.domain.analytics.abstraction.HistoryExerciseSetCacheService
import com.mikolove.core.domain.analytics.HistoryExerciseSet

class HistoryExerciseSetCacheDataSourceImpl
constructor(private val historyExerciseSetCacheService : HistoryExerciseSetCacheService)
    : HistoryExerciseSetCacheDataSource {

    override suspend fun insertHistoryExerciseSet(historyExerciseSet: HistoryExerciseSet, historyExerciseId: String): Long = historyExerciseSetCacheService.insertHistoryExerciseSet(historyExerciseSet,historyExerciseId)

    override suspend fun getHistoryExerciseSetById(idHistoryExerciseSet: String): HistoryExerciseSet? = historyExerciseSetCacheService.getHistoryExerciseSetById(idHistoryExerciseSet)

    override suspend fun getHistoryExerciseSetsByHistoryExercise(idHistoryExercise: String): List<HistoryExerciseSet>? = historyExerciseSetCacheService.getHistoryExerciseSetsByHistoryExercise(idHistoryExercise)

}
