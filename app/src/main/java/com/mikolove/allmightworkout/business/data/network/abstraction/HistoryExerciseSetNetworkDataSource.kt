package com.mikolove.allmightworkout.business.data.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.HistoryExerciseSet

interface HistoryExerciseSetNetworkDataSource {

    suspend fun insertHistoryExerciseSet(historyExerciseSet: HistoryExerciseSet, historyExerciseId: String)

    suspend fun updateHistoryExerciseSet(historyExerciseSet: HistoryExerciseSet)

    suspend fun getHistoryExerciseSetsByHistoryExerciseId(idHistoryExerciseId : Long) : List<HistoryExerciseSet>

}