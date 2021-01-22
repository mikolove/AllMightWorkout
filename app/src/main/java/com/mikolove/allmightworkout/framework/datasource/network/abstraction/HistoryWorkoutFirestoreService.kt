package com.mikolove.allmightworkout.framework.datasource.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout

interface HistoryWorkoutFirestoreService {

    suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout)

    suspend fun updateHistoryWorkout(historyWorkout: HistoryWorkout)

    suspend fun getLastHistoryWorkout() : HistoryWorkout?

    suspend fun getHistoryWorkout() : List<HistoryWorkout>

    suspend fun getHistoryWorkoutById(primaryKey : Long) : HistoryWorkout?
}