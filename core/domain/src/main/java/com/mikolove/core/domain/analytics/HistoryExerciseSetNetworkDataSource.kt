package com.mikolove.core.domain.analytics

interface HistoryExerciseSetNetworkDataSource {

    suspend fun insertHistoryExerciseSet(
        historyExerciseSet: HistoryExerciseSet,
        historyExerciseId: String,
        historyWorkoutId: String
    )

    suspend fun getHistoryExerciseSetsByHistoryExerciseId(
        idHistoryExerciseId: String,
        idHistoryWorkout: String
    ) : List<HistoryExerciseSet>

}