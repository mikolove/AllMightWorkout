package com.mikolove.allmightworkout.business.data.network.abstraction

import com.mikolove.core.domain.analytics.HistoryExercise

interface HistoryExerciseNetworkDataSource {

    suspend fun insertHistoryExercise(historyExercise: HistoryExercise, idHistoryWorkout: String)

    suspend fun getHistoryExercisesByHistoryWorkoutId(workoutId: String) : List<HistoryExercise>

    suspend fun getHistoryExerciseById(primaryKey: String, idHistoryWorkout: String) : HistoryExercise?

}
