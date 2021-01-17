package com.mikolove.allmightworkout.business.data.cache.implementation

import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryExerciseSetCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.HistoryExerciseSet
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.HistoryExerciseSetDaoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryExerciseSetCacheDataSourceImpl
@Inject
constructor(private val historyExerciseSetDaoService : HistoryExerciseSetDaoService)
    : HistoryExerciseSetCacheDataSource {

    override suspend fun insertHistoryExerciseSet(historyExerciseSet: HistoryExerciseSet): Long = historyExerciseSetDaoService.insertHistoryExerciseSet(historyExerciseSet)

    override suspend fun getHistoryExerciseSetsByHistoryExercise(idHistoryExercise: String): List<HistoryExerciseSet>? = historyExerciseSetDaoService.getHistoryExerciseSetsByHistoryExercise(idHistoryExercise)

    override suspend fun getTotalHistoryExerciseSet(): Int = historyExerciseSetDaoService.getTotalHistoryExerciseSet()
}
