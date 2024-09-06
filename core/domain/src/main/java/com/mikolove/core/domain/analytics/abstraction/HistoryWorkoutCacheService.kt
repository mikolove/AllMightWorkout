package com.mikolove.core.domain.analytics.abstraction

import com.mikolove.core.domain.analytics.HistoryWorkout

interface HistoryWorkoutCacheService {

    suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout, idUser : String) : Long

    suspend fun deleteHistoryWorkout(idHistoryWorkout: String) : Int

    suspend fun getHistoryWorkoutById(primaryKey : String) : HistoryWorkout

    suspend fun getHistoryWorkouts(idUser: String) : List<HistoryWorkout>

}