package com.mikolove.allmightworkout.framework.datasource.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.HistoryExercise

interface HistoryExerciseDaoService {

    suspend fun insertHistoryExercise(historyExercise: HistoryExercise) : Long

    suspend fun getHistoryExercisesByHistoryWorkout(idHistoryWorkout: String): List<HistoryExercise>?

    suspend fun getHistoryExerciseById(primaryKey : String) : HistoryExercise?

    suspend fun getTotalHistoryExercise() : Int

}