package com.mikolove.core.database.implementation

import com.mikolove.core.database.database.AnalyticsDao
import com.mikolove.core.database.mappers.toHistoryExerciseCacheEntity
import com.mikolove.core.database.mappers.toHistoryExerciseSetCacheEntity
import com.mikolove.core.database.mappers.toHistoryWorkout
import com.mikolove.core.database.mappers.toHistoryWorkoutCacheEntity
import com.mikolove.core.domain.analytics.HistoryExercise
import com.mikolove.core.domain.analytics.HistoryExerciseSet
import com.mikolove.core.domain.analytics.HistoryWorkout
import com.mikolove.core.domain.analytics.abstraction.AnalyticsCacheService

class AnalyticsDaoService(
    private val analyticsDao : AnalyticsDao,
) : AnalyticsCacheService {

    override suspend fun insertHistoryWorkout(
        historyWorkout: HistoryWorkout,
        idUser: String
    ): Long {
        return analyticsDao.insertHistoryWorkout(historyWorkout.toHistoryWorkoutCacheEntity(idUser))
    }

    override suspend fun deleteHistoryWorkout(idHistoryWorkout: String): Int {
        return analyticsDao.deleteHistoryWorkout(idHistoryWorkout)
    }

    override suspend fun getHistoryWorkoutById(primaryKey: String): HistoryWorkout {
        return analyticsDao.getHistoryWorkoutById(primaryKey).toHistoryWorkout()
    }

    override suspend fun getHistoryWorkouts(idUser: String): List<HistoryWorkout> {
        return analyticsDao.getHistoryWorkouts(idUser).map { it.toHistoryWorkout()}
    }

    override suspend fun insertHistoryExercise(
        historyExercise: HistoryExercise,
        idHistoryWorkout: String
    ): Long {
        return analyticsDao.insertHistoryExercise(historyExercise.toHistoryExerciseCacheEntity(idHistoryWorkout))
    }

    override suspend fun insertHistoryExerciseSet(
        historyExerciseSet: HistoryExerciseSet,
        historyExerciseId: String
    ): Long {
        return analyticsDao.insertHistoryExerciseSet(historyExerciseSet.toHistoryExerciseSetCacheEntity(historyExerciseId))
    }

}