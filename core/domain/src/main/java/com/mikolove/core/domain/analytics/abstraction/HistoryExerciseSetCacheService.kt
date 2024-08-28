package com.mikolove.core.domain.analytics.abstraction

import com.mikolove.core.domain.analytics.HistoryExerciseSet

interface HistoryExerciseSetCacheService {

    suspend fun insertHistoryExerciseSet(historyExerciseSet: HistoryExerciseSet, historyExerciseId: String) : Long

    suspend fun getHistoryExerciseSetById(idHistoryExerciseSet : String) : HistoryExerciseSet?

    suspend fun getHistoryExerciseSetsByHistoryExercise(idHistoryExercise : String) : List<HistoryExerciseSet>

    suspend fun getTotalHistoryExerciseSet(idHistoryExercise: String): Int
}