package com.mikolove.allmightworkout.business.data.network.implementation

import com.mikolove.allmightworkout.business.data.network.abstraction.HistoryExerciseNetworkDataSource
import com.mikolove.core.domain.analytics.HistoryExercise
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.HistoryExerciseFirestoreService

class HistoryExerciseNetworkDataSourceImpl
constructor(private val historyExerciseFirestoreService : HistoryExerciseFirestoreService) : HistoryExerciseNetworkDataSource{

    override suspend fun insertHistoryExercise(
        historyExercise: HistoryExercise,
        idHistoryWorkout: String
    ) = historyExerciseFirestoreService.insertHistoryExercise(historyExercise,idHistoryWorkout)

    override suspend fun getHistoryExercisesByHistoryWorkoutId(workoutId: String): List<HistoryExercise> = historyExerciseFirestoreService.getHistoryExerciseByHistoryWorkoutId(workoutId)

    override suspend fun getHistoryExerciseById(primaryKey: String, idHistoryWorkout: String): HistoryExercise? = historyExerciseFirestoreService.getHistoryExerciseById(
        primaryKey,idHistoryWorkout
    )
}
