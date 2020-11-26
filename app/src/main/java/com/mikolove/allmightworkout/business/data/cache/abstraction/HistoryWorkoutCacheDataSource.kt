package com.mikolove.allmightworkout.business.data.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout

interface HistoryWorkoutCacheDataSource {

    suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout) : Long

    suspend fun getHistoryWorkouts(query : String, filterAndOrder : String, page : Int) : List<HistoryWorkout>

    suspend fun getHistoryWorkoutById(workoutId : String) : HistoryWorkout?

    suspend fun getTotalHistoryWorkout() : Int
}