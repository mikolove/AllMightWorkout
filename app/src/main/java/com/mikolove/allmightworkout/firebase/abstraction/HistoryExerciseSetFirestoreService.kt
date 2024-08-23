package com.mikolove.allmightworkout.firebase.abstraction

import com.mikolove.core.domain.analytics.HistoryExerciseSet

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