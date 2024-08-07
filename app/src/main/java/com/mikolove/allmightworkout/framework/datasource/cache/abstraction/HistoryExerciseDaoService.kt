package com.mikolove.allmightworkout.framework.datasource.cache.abstraction

import com.mikolove.core.domain.analytics.HistoryExercise

interface HistoryExerciseDaoService {

    suspend fun insertHistoryExercise(historyExercise: HistoryExercise, idHistoryWorkout: String) : Long

    suspend fun getHistoryExercisesByHistoryWorkout(idHistoryWorkout: String): List<HistoryExercise>?

    suspend fun getHistoryExerciseById(primaryKey : String) : HistoryExercise?

    suspend fun getTotalHistoryExercise() : Int

}