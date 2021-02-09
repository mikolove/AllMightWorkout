package com.mikolove.allmightworkout.framework.datasource.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.HistoryExerciseSet

interface HistoryExerciseSetDaoService {

    suspend fun insertHistoryExerciseSet(historyExerciseSet: HistoryExerciseSet, historyExerciseId: String) : Long

    suspend fun getHistoryExerciseSetById(idHistoryExerciseSet : String) : HistoryExerciseSet?

    suspend fun getHistoryExerciseSetsByHistoryExercise(idHistoryExercise : String) : List<HistoryExerciseSet>

    suspend fun getTotalHistoryExerciseSet(idHistoryExercise: String): Int
}