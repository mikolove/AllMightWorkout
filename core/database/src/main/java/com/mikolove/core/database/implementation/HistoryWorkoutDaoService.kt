package com.mikolove.core.database.implementation

import com.mikolove.core.database.database.HistoryWorkoutDao
import com.mikolove.core.database.mappers.toHistoryWorkout
import com.mikolove.core.database.mappers.toHistoryWorkoutCacheEntity
import com.mikolove.core.domain.analytics.HistoryWorkout
import com.mikolove.core.domain.analytics.abstraction.HistoryWorkoutCacheService

class HistoryWorkoutDaoService(
    private val historyWorkoutDao : HistoryWorkoutDao,
) : HistoryWorkoutCacheService {

    override suspend fun insertHistoryWorkout(
        historyWorkout: HistoryWorkout,
        idUser: String
    ): Long {
        return historyWorkoutDao.insertHistoryWorkout(historyWorkout.toHistoryWorkoutCacheEntity(idUser))
    }

    override suspend fun deleteHistoryWorkout(idHistoryWorkout: String): Int {
        return historyWorkoutDao.deleteHistoryWorkout(idHistoryWorkout)
    }

    override suspend fun getHistoryWorkoutById(primaryKey: String): HistoryWorkout {
        return historyWorkoutDao.getHistoryWorkoutById(primaryKey).toHistoryWorkout()
    }

    override suspend fun getHistoryWorkouts(idUser: String): List<HistoryWorkout> {
        return historyWorkoutDao.getHistoryWorkouts(idUser).map { it.toHistoryWorkout()}
    }
}