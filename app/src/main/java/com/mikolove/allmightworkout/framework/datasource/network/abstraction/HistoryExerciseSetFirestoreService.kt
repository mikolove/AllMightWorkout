package com.mikolove.allmightworkout.framework.datasource.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.HistoryExerciseSet

interface HistoryExerciseSetFirestoreService {

    suspend fun insertHistoryExerciseSet(historyExerciseSet: HistoryExerciseSet)

    suspend fun updateHistoryExerciseSet(historyExerciseSet: HistoryExerciseSet)

    suspend fun getHistoryExerciseSetsByHistoryExerciseId(idHistoryExerciseId : Long) : List<HistoryExerciseSet>

}