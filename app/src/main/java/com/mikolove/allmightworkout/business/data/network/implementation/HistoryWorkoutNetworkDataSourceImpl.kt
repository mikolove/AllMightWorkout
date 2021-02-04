package com.mikolove.allmightworkout.business.data.network.implementation

import com.mikolove.allmightworkout.business.data.network.abstraction.HistoryWorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.HistoryWorkoutFirestoreService
import javax.inject.Inject
import javax.inject.Singleton


class HistoryWorkoutNetworkDataSourceImpl
constructor(private val historyWorkoutFirestoreService : HistoryWorkoutFirestoreService) : HistoryWorkoutNetworkDataSource{

    override suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout) = historyWorkoutFirestoreService.insertHistoryWorkout(historyWorkout)

    override suspend fun getLastHistoryWorkouts(): List<HistoryWorkout>? = historyWorkoutFirestoreService.getLastHistoryWorkout()

    override suspend fun getHistoryWorkout(): List<HistoryWorkout>? = historyWorkoutFirestoreService.getHistoryWorkout()

    override suspend fun getHistoryWorkoutById(primaryKey: String): HistoryWorkout? = historyWorkoutFirestoreService.getHistoryWorkoutById(primaryKey)
}
