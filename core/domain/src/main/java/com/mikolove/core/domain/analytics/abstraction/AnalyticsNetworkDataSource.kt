package com.mikolove.core.domain.analytics.abstraction

import com.mikolove.core.domain.analytics.HistoryWorkout

interface AnalyticsNetworkDataSource {

    suspend fun insertHistoryWorkouts(historyWorkout : HistoryWorkout)

    suspend fun removeHistoryWorkouts(historyWorkout: HistoryWorkout)

    suspend fun getHistoryWorkouts() : List<HistoryWorkout>
}