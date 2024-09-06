package com.mikolove.core.database.implementation

import com.mikolove.core.database.database.HistoryExerciseSetDao
import com.mikolove.core.database.mappers.toHistoryExerciseSetCacheEntity
import com.mikolove.core.domain.analytics.HistoryExerciseSet
import com.mikolove.core.domain.analytics.abstraction.HistoryExerciseSetCacheService

class HistoryExerciseSetDaoService(
    private val historyExerciseSetDao : HistoryExerciseSetDao,
) : HistoryExerciseSetCacheService {

    override suspend fun insertHistoryExerciseSet(
        historyExerciseSet: HistoryExerciseSet,
        historyExerciseId: String
    ): Long {
        return historyExerciseSetDao.insertHistoryExerciseSet(historyExerciseSet.toHistoryExerciseSetCacheEntity(historyExerciseId))
    }

}