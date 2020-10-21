package com.mikolove.allmightworkout.business.domain.data.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.HistoryExercise

interface HistoryExerciseCacheDataSource {

    suspend fun insertHistoryExercise(historyExercise: HistoryExercise) : Long

    suspend fun updateHistoryExercise(historyExercise: HistoryExercise) : Int

    suspend fun getHistoryExercise(query : String, filterAndOrder : String, page : Int) : List<HistoryExercise>

    suspend fun getHistoryExerciseById(primaryKey : Long) : HistoryExercise?

}