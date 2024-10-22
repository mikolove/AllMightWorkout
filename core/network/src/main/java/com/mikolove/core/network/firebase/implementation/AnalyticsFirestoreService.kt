package com.mikolove.core.network.firebase.implementation

import com.mikolove.core.domain.analytics.HistoryWorkout
import com.mikolove.core.domain.analytics.abstraction.AnalyticsNetworkService
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.network.firebase.mappers.AnalyticsNetworkMapper

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