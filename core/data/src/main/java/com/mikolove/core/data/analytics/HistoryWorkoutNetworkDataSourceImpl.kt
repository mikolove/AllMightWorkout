package com.mikolove.core.data.analytics

import com.mikolove.core.domain.analytics.HistoryWorkoutNetworkDataSource
import com.mikolove.core.domain.analytics.HistoryWorkout
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.HistoryWorkoutFirestoreService


class HistoryWorkoutNetworkDataSourceImpl
constructor(private val historyWorkoutFirestoreService : HistoryWorkoutFirestoreService) :
    HistoryWorkoutNetworkDataSource {

    override suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout) = historyWorkoutFirestoreService.insertHistoryWorkout(historyWorkout)

    override suspend fun getLastHistoryWorkouts(): List<HistoryWorkout>? = historyWorkoutFirestoreService.getLastHistoryWorkout()

    override suspend fun getHistoryWorkout(): List<HistoryWorkout>? = historyWorkoutFirestoreService.getHistoryWorkout()

    override suspend fun getHistoryWorkoutById(primaryKey: String): HistoryWorkout? = historyWorkoutFirestoreService.getHistoryWorkoutById(primaryKey)
}
