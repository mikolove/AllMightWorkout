package com.mikolove.allmightworkout.framework.datasource.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout

interface HistoryWorkoutFirestoreService {

    suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout)

    suspend fun getLastHistoryWorkout() : List<HistoryWorkout>?

    suspend fun getHistoryWorkout() : List<HistoryWorkout>

    suspend fun getHistoryWorkoutById(primaryKey: String) : HistoryWorkout?
}