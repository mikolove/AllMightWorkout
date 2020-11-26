package com.mikolove.allmightworkout.business.data.network.implementation

import com.mikolove.allmightworkout.business.data.network.abstraction.HistoryExerciseNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.HistoryExercise
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.HistoryExerciseFirestoreService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryExerciseNetworkDataSourceImpl
@Inject
constructor(private val historyExerciseFirestoreService : HistoryExerciseFirestoreService) : HistoryExerciseNetworkDataSource{

    override suspend fun insertHistoryExercise(historyExercise: HistoryExercise) = historyExerciseFirestoreService.insertHistoryExercise(historyExercise)

    override suspend fun updateHistoryExercise(historyExercise: HistoryExercise) = historyExerciseFirestoreService.updateHistoryExercise(historyExercise)

    override suspend fun getHistoryExercisesByHistoryWorkoutId(workoutId : Long): List<HistoryExercise> = historyExerciseFirestoreService.getHistoryExerciseByHistoryWorkoutId(workoutId)

    override suspend fun getHistoryExerciseById(primaryKey: Long): HistoryExercise? = historyExerciseFirestoreService.getHistoryExerciseById(primaryKey)
}
