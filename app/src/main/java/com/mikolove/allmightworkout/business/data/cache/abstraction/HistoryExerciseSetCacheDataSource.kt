package com.mikolove.allmightworkout.business.data.cache.abstraction

import com.mikolove.core.domain.analytics.HistoryExerciseSet

interface HistoryExerciseSetCacheDataSource {

    suspend fun insertHistoryExerciseSet(historyExerciseSet: HistoryExerciseSet, historyExerciseId: String) : Long

    suspend fun getHistoryExerciseSetById(idHistoryExerciseSet : String) : HistoryExerciseSet?

    suspend fun getHistoryExerciseSetsByHistoryExercise(idHistoryExercise: String): List<HistoryExerciseSet>?

    suspend fun getTotalHistoryExerciseSet(idHistoryExercise: String): Int

}