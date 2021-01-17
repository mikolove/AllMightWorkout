package com.mikolove.allmightworkout.framework.datasource.cache.abstraction

import com.mikolove.allmightEXERCISE.framework.datasource.database.HISTORY_EXERCISE_PAGINATION_PAGE_SIZE
import com.mikolove.allmightworkout.business.domain.model.HistoryExercise

interface HistoryExerciseDaoService {

    suspend fun insertHistoryExercise(historyExercise: HistoryExercise) : Long

    suspend fun updateHistoryExercise(historyExercise: HistoryExercise) : Int

    suspend fun getHistoryExercisesByHistoryWorkout(idHistoryWorkout: String): List<HistoryExercise>?

    suspend fun getHistoryExerciseById(primaryKey : String) : HistoryExercise?

    suspend fun getTotalHistoryExercise() : Int

}