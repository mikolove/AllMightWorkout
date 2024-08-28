package com.mikolove.core.data.repositories.analytics

import com.mikolove.core.domain.analytics.HistoryWorkout
import com.mikolove.core.domain.analytics.abstraction.AnalyticsNetworkDataSource
import com.mikolove.core.domain.analytics.abstraction.AnalyticsNetworkService

data class AnalyticsNetworkDataSourceImpl (
    private val analyticsNetworkService : AnalyticsNetworkService
) : AnalyticsNetworkDataSource {

    override suspend fun insertHistoryWorkouts(historyWorkout: HistoryWorkout)
        = analyticsNetworkService.insertHistoryWorkouts(historyWorkout)

    override suspend fun removeHistoryWorkouts(historyWorkout: HistoryWorkout)
        = analyticsNetworkService.removeHistoryWorkouts(historyWorkout)

    override suspend fun getHistoryWorkouts(): List<HistoryWorkout>
        = analyticsNetworkService.getHistoryWorkouts()
}