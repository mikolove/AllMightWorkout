package com.mikolove.core.data.analytics

import com.mikolove.core.domain.analytics.HistoryExerciseNetworkDataSource
import com.mikolove.core.domain.analytics.HistoryExercise
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.HistoryExerciseFirestoreService

class HistoryExerciseNetworkDataSourceImpl
constructor(private val historyExerciseFirestoreService : HistoryExerciseFirestoreService) :
    HistoryExerciseNetworkDataSource {

    override suspend fun insertHistoryExercise(
        historyExercise: HistoryExercise,
        idHistoryWorkout: String
    ) = historyExerciseFirestoreService.insertHistoryExercise(historyExercise,idHistoryWorkout)

    override suspend fun getHistoryExercisesByHistoryWorkoutId(workoutId: String): List<HistoryExercise> = historyExerciseFirestoreService.getHistoryExerciseByHistoryWorkoutId(workoutId)

    override suspend fun getHistoryExerciseById(primaryKey: String, idHistoryWorkout: String): HistoryExercise? = historyExerciseFirestoreService.getHistoryExerciseById(
        primaryKey,idHistoryWorkout
    )
}
