package com.mikolove.allmightworkout.business.data.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.HistoryExercise

interface HistoryExerciseNetworkDataSource {

    suspend fun insertHistoryExercise(historyExercise: HistoryExercise)

    suspend fun updateHistoryExercise(historyExercise: HistoryExercise)

    suspend fun getHistoryExercisesByHistoryWorkoutId(workoutId : Long) : List<HistoryExercise>

    suspend fun getHistoryExerciseById( primaryKey : Long) : HistoryExercise?

}
