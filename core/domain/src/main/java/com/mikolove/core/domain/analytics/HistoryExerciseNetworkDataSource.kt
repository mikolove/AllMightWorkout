package com.mikolove.core.domain.analytics

interface HistoryExerciseNetworkDataSource {

    suspend fun insertHistoryExercise(historyExercise: HistoryExercise, idHistoryWorkout: String)

    suspend fun getHistoryExercisesByHistoryWorkoutId(workoutId: String) : List<HistoryExercise>

    suspend fun getHistoryExerciseById(primaryKey: String, idHistoryWorkout: String) : HistoryExercise?

}
