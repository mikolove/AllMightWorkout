package com.mikolove.allmightworkout.firebase.abstraction

import com.mikolove.core.domain.analytics.HistoryExercise

interface HistoryExerciseFirestoreService {

    suspend fun insertHistoryExercise(historyExercise: HistoryExercise, idHistoryWorkout: String)

    suspend fun getHistoryExercisesById(idHistoryWorkout: String) : List<HistoryExercise>

    suspend fun getHistoryExerciseById(idHistoryExercise: String, idHistoryWorkout: String) : HistoryExercise?

}