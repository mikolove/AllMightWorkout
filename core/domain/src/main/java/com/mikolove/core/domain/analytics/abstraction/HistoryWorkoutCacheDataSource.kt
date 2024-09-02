package com.mikolove.core.domain.analytics.abstraction

import com.mikolove.core.domain.analytics.HistoryWorkout

interface HistoryWorkoutCacheDataSource {

    suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout, idUser : String) : Long

    suspend fun deleteHistoryWorkout(idHistoryWorkout : String) : Int

    suspend fun getHistoryWorkouts(query : String, filterAndOrder : String, page : Int, idUser : String) : List<HistoryWorkout>

    suspend fun getHistoryWorkoutById(historyWorkoutId : String) : HistoryWorkout?
}