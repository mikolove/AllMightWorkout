package com.mikolove.core.network.abstraction

import com.mikolove.core.domain.analytics.HistoryExercise

interface HistoryExerciseFirestoreService {

    suspend fun insertHistoryExercise(historyExercise: HistoryExercise, idHistoryWorkout: String)

    suspend fun getHistoryExerciseByHistoryWorkoutId(workoutId: String) : List<HistoryExercise>

    suspend fun getHistoryExerciseById(primaryKey: String, idHistoryWorkout: String) : HistoryExercise?

}