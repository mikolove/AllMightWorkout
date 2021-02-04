package com.mikolove.allmightworkout.business.data.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.HistoryExercise

interface HistoryExerciseNetworkDataSource {

    suspend fun insertHistoryExercise(historyExercise: HistoryExercise, idHistoryWorkout: String)

    suspend fun getHistoryExercisesByHistoryWorkoutId(workoutId: String) : List<HistoryExercise>

    suspend fun getHistoryExerciseById(primaryKey: String, idHistoryWorkout: String) : HistoryExercise?

}
