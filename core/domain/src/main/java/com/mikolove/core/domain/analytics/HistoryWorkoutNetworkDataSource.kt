package com.mikolove.core.domain.analytics

interface HistoryWorkoutNetworkDataSource {

    suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout)

    suspend fun getLastHistoryWorkouts(): List<HistoryWorkout>?

    suspend fun getHistoryWorkout(): List<HistoryWorkout>?

    suspend fun getHistoryWorkoutById(primaryKey: String) : HistoryWorkout?
}