package com.mikolove.allmightworkout.framework.datasource.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.HistoryExerciseSet

interface HistoryExerciseSetDaoService {

    suspend fun insertHistoryExerciseSet(historyExerciseSet: HistoryExerciseSet) : Long

    suspend fun updateHistoryExerciseSet(historyExerciseSet: HistoryExerciseSet) : Int

    suspend fun getHistoryExerciseSetsByHistoryExercise(idHistoryExercise : String) : List<HistoryExerciseSet>

    suspend fun getTotalHistoryExerciseSet() : Int
}