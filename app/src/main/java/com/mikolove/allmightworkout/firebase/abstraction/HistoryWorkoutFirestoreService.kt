package com.mikolove.allmightworkout.firebase.abstraction

import com.mikolove.core.domain.analytics.HistoryWorkout

interface HistoryWorkoutFirestoreService {

    suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout)

    suspend fun getLastHistoryWorkout() : List<HistoryWorkout>?

    suspend fun getHistoryWorkout() : List<HistoryWorkout>

    suspend fun getHistoryWorkoutById(primaryKey: String) : HistoryWorkout?
}