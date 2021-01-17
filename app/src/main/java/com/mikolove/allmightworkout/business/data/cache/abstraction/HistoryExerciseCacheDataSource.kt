package com.mikolove.allmightworkout.business.data.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.HistoryExercise

interface HistoryExerciseCacheDataSource {

    suspend fun insertHistoryExercise(historyExercise: HistoryExercise) : Long

    suspend fun updateHistoryExercise(historyExercise: HistoryExercise) : Int

    suspend fun getHistoryExercisesByHistoryWorkout( idHistoryWorkout: String) : List<HistoryExercise>?

    suspend fun getHistoryExerciseById(primaryKey : String) : HistoryExercise?

    suspend fun getTotalHistoryExercise() : Int

}