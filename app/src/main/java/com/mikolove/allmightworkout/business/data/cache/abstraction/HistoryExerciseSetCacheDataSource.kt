package com.mikolove.allmightworkout.business.data.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.HistoryExerciseSet

interface HistoryExerciseSetCacheDataSource {

    suspend fun insertHistoryExerciseSet(historyExerciseSet: HistoryExerciseSet) : Long

    suspend fun getHistoryExerciseSetsByHistoryExercise(idHistoryExercise : String) : List<HistoryExerciseSet>

    suspend fun getTotalHistoryExerciseSet() : Int

}