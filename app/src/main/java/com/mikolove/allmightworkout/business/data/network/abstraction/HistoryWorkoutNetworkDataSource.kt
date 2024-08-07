package com.mikolove.allmightworkout.business.data.network.abstraction

import com.mikolove.core.domain.analytics.HistoryWorkout

interface HistoryWorkoutNetworkDataSource {

    suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout)

    suspend fun getLastHistoryWorkouts(): List<HistoryWorkout>?

    suspend fun getHistoryWorkout(): List<HistoryWorkout>?

    suspend fun getHistoryWorkoutById(primaryKey: String) : HistoryWorkout?
}