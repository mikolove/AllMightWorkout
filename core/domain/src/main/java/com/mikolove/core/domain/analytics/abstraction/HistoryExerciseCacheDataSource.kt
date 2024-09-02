package com.mikolove.core.domain.analytics.abstraction

import com.mikolove.core.domain.analytics.HistoryExercise

interface HistoryExerciseCacheDataSource {

    suspend fun insertHistoryExercise(historyExercise: HistoryExercise, idHistoryWorkout: String) : Long

    suspend fun getHistoryExercisesByHistoryWorkout( idHistoryWorkout: String) : List<HistoryExercise>?

    suspend fun getHistoryExerciseById(primaryKey : String) : HistoryExercise?
    
}