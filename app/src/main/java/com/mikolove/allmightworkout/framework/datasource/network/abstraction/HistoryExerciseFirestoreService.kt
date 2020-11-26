package com.mikolove.allmightworkout.framework.datasource.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.HistoryExercise

interface HistoryExerciseFirestoreService {

    suspend fun insertHistoryExercise(historyExercise: HistoryExercise)

    suspend fun updateHistoryExercise(historyExercise: HistoryExercise)

    suspend fun getHistoryExerciseByHistoryWorkoutId(workoutId : Long) : List<HistoryExercise>

    suspend fun getHistoryExerciseById(primaryKey : Long) : HistoryExercise?

}