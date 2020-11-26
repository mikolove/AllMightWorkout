package com.mikolove.allmightworkout.business.data.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.HistoryExercise

interface HistoryExerciseCacheDataSource {

    suspend fun insertHistoryExercise(historyExercise: HistoryExercise) : Long

    suspend fun getHistoryExercises(query : String, filterAndOrder : String, page : Int) : List<HistoryExercise>

    suspend fun getHistoryExerciseById(primaryKey : String) : HistoryExercise?

    suspend fun getTotalHistoryExercise() : Int

}