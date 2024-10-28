package com.mikolove.core.data.datasource.analytics

import com.mikolove.core.domain.analytics.HistoryExercise
import com.mikolove.core.domain.analytics.HistoryExerciseSet
import com.mikolove.core.domain.analytics.HistoryWorkout
import com.mikolove.core.domain.analytics.abstraction.AnalyticsCacheDataSource
import com.mikolove.core.domain.analytics.abstraction.AnalyticsCacheService

class AnalyticsCacheDataSourceImpl
constructor(private val analyticsCacheService : AnalyticsCacheService)
    : AnalyticsCacheDataSource {

    override suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout, idUser : String): Long = analyticsCacheService.insertHistoryWorkout(historyWorkout,idUser)

    override suspend fun deleteHistoryWorkout(idHistoryWorkout: String): Int = analyticsCacheService.deleteHistoryWorkout(idHistoryWorkout)

    override suspend fun getHistoryWorkouts(idUser : String): List<HistoryWorkout> = analyticsCacheService.getHistoryWorkouts(idUser)

    override suspend fun getHistoryWorkoutById(historyWorkoutId: String): HistoryWorkout = analyticsCacheService.getHistoryWorkoutById(historyWorkoutId)

    override suspend fun insertHistoryExercise(
        historyExercise: HistoryExercise,
        idHistoryWorkout: String
    ): Long = analyticsCacheService.insertHistoryExercise(historyExercise,idHistoryWorkout)

    override suspend fun insertHistoryExerciseSet(
        historyExerciseSet: HistoryExerciseSet,
        idHistoryExercise: String
    ): Long = analyticsCacheService.insertHistoryExerciseSet(historyExerciseSet, idHistoryExercise)
}
