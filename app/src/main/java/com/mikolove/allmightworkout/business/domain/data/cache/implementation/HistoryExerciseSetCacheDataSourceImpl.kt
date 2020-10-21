package com.mikolove.allmightworkout.business.domain.data.cache.implementation

import com.mikolove.allmightworkout.business.domain.data.cache.abstraction.HistoryExerciseSetCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.HistoryExerciseSet
import javax.inject.Inject
import javax.inject.Singleton

/*
@Singleton
class HistoryExerciseSetCacheDataSourceImpl
@Inject
constructor(private val historyExerciseSetDaoService : HistoryExerciseSetDaoService)
    : HistoryExerciseSetCacheDataSource {

    override suspend fun insertHistoryExerciseSet(historyExerciseSet: HistoryExerciseSet): Long = historyExerciseSetDaoService.insertHistoryExerciseSet(historyExerciseSet)

    override suspend fun updateHistoryExerciseSet(historyExerciseSet: HistoryExerciseSet): Int = historyExerciseSetDaoService.updateHistoryExerciseSet(historyExerciseSet)

    override suspend fun getHistoryExerciseSet(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<HistoryExerciseSet> = historyExerciseSetDaoService.getHistoryExerciseSet(query,filterAndOrder,page)

    override suspend fun getHistoryExerciseSetById(primareyKey: Long): HistoryExerciseSet? = historyExerciseSetDaoService.getHistoryExerciseSetById(primareyKey)
}*/
