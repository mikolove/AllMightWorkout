package com.mikolove.core.domain.analytics

interface HistoryExerciseCacheDataSource {

    suspend fun insertHistoryExercise(historyExercise: HistoryExercise, idHistoryWorkout: String) : Long

    suspend fun getHistoryExercisesByHistoryWorkout( idHistoryWorkout: String) : List<HistoryExercise>?

    suspend fun getHistoryExerciseById(primaryKey : String) : HistoryExercise?

    suspend fun getTotalHistoryExercise() : Int

}