package com.mikolove.allmightworkout.framework.datasource.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.HistoryExerciseSet

interface HistoryExerciseSetFirestoreService {

    suspend fun insertHistoryExerciseSet(
        historyExerciseSet: HistoryExerciseSet,
        historyExerciseId: String,
        historyWorkoutId: String
    )

    suspend fun getHistoryExerciseSetsByHistoryExerciseId(
        idHistoryExerciseId: String,
        idHistoryWorkoutId: String
    ) : List<HistoryExerciseSet>

}