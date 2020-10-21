package com.mikolove.allmightworkout.business.data.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout

interface HistoryWorkoutCacheDataSource {

    suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout) : Long

    suspend fun updateHistoryWorkout(historyWorkout: HistoryWorkout) : Int

    suspend fun getHistoryWorkout(query : String, filterAndOrder : String, page : Int) : List<HistoryWorkout>

    suspend fun getHistoryWorkoutById(primaryKey : Long) : HistoryWorkout?
}