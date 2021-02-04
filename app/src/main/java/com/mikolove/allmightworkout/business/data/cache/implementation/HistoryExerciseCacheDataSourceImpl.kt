package com.mikolove.allmightworkout.business.data.cache.implementation

import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryExerciseCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.HistoryExercise
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.HistoryExerciseDaoService

class HistoryExerciseCacheDataSourceImpl
constructor(private val historyExerciseDaoService : HistoryExerciseDaoService)
    : HistoryExerciseCacheDataSource {

    override suspend fun insertHistoryExercise(
        historyExercise: HistoryExercise,
        idHistoryWorkout: String
    ): Long = historyExerciseDaoService.insertHistoryExercise(
        historyExercise,
        idHistoryWorkout
    )

    override suspend fun getHistoryExercisesByHistoryWorkout(idHistoryWorkout: String): List<HistoryExercise>? = historyExerciseDaoService.getHistoryExercisesByHistoryWorkout(idHistoryWorkout)

    override suspend fun getHistoryExerciseById(primaryKey: String): HistoryExercise? = historyExerciseDaoService.getHistoryExerciseById(primaryKey)

    override suspend fun getTotalHistoryExercise(): Int = historyExerciseDaoService.getTotalHistoryExercise()
}
