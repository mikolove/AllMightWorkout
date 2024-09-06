package com.mikolove.core.domain.analytics.abstraction

import com.mikolove.core.domain.analytics.HistoryExercise

interface HistoryExerciseCacheService {

    suspend fun insertHistoryExercise(historyExercise: HistoryExercise, idHistoryWorkout: String) : Long
}