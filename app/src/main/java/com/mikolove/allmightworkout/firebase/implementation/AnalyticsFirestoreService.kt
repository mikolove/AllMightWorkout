package com.mikolove.allmightworkout.firebase.implementation

import com.mikolove.allmightworkout.firebase.mappers.AnalyticsNetworkMapper
import com.mikolove.core.domain.analytics.abstraction.AnalyticsNetworkService
import com.mikolove.core.domain.analytics.HistoryWorkout
import com.mikolove.core.domain.auth.SessionStorage

data class AnalyticsFirestoreService(
    private val sessionStorage: SessionStorage,
    private val analyticsMapper : AnalyticsNetworkMapper
) : AnalyticsNetworkService {

    override suspend fun insertHistoryWorkouts(historyWorkout: HistoryWorkout) {
        TODO("Not yet implemented")
    }

    override suspend fun removeHistoryWorkouts(historyWorkout: HistoryWorkout) {
        TODO("Not yet implemented")
    }

    override suspend fun getHistoryWorkouts(): List<HistoryWorkout> {
        TODO("Not yet implemented")
    }
}