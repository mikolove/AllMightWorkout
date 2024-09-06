package com.mikolove.core.database.implementation

import com.mikolove.core.database.database.HistoryExerciseDao
import com.mikolove.core.database.mappers.toHistoryExerciseCacheEntity
import com.mikolove.core.domain.analytics.HistoryExercise
import com.mikolove.core.domain.analytics.abstraction.HistoryExerciseCacheService

class HistoryExerciseDaoService(
    private val historyExerciseDao : HistoryExerciseDao,
) : HistoryExerciseCacheService {

    override suspend fun insertHistoryExercise(
        historyExercise: HistoryExercise,
        idHistoryWorkout: String
    ): Long {
        return historyExerciseDao.insertHistoryExercise(historyExercise.toHistoryExerciseCacheEntity(idHistoryWorkout))
    }
}