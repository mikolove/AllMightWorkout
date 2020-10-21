package com.mikolove.allmightworkout.business.domain.data.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.HistoryExerciseSet

interface HistoryExerciseSetCacheDataSource {

    suspend fun insertHistoryExerciseSet(historyExerciseSet: HistoryExerciseSet) : Long

    suspend fun updateHistoryExerciseSet(historyExerciseSet: HistoryExerciseSet) : Int

    suspend fun getHistoryExerciseSet(query : String, filterAndOrder : String, page : Int) : List<HistoryExerciseSet>

    suspend fun getHistoryExerciseSetById(primareyKey : Long) : HistoryExerciseSet?


}