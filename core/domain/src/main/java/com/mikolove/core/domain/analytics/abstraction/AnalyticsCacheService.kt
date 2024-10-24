package com.mikolove.core.domain.analytics.abstraction

import com.mikolove.core.domain.analytics.HistoryExercise
import com.mikolove.core.domain.analytics.HistoryExerciseSet
import com.mikolove.core.domain.analytics.HistoryWorkout

interface AnalyticsCacheService {

    suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout, idUser : String) : Long

    suspend fun deleteHistoryWorkout(idHistoryWorkout: String) : Int

    suspend fun getHistoryWorkouts(idUser: String) : List<HistoryWorkout>

    suspend fun getHistoryWorkoutById(primaryKey : String) : HistoryWorkout

    suspend fun insertHistoryExercise(historyExercise : HistoryExercise, idHistoryWorkout: String) : Long

    suspend fun insertHistoryExerciseSet(historyExerciseSet: HistoryExerciseSet, idHistoryExercise : String) : Long

}