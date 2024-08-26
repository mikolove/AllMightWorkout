package com.mikolove.core.domain.analytics

interface AnalyticsNetworkDataSource {

    suspend fun insertHistoryWorkouts(historyWorkout : HistoryWorkout)

    suspend fun removeHistoryWorkouts(historyWorkout: HistoryWorkout)

    suspend fun getHistoryWorkouts() : List<HistoryWorkout>
}