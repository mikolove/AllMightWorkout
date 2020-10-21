package com.mikolove.allmightworkout.business.domain.data.cache.implementation

import com.mikolove.allmightworkout.business.domain.data.cache.abstraction.HistoryExerciseCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.HistoryExercise
import javax.inject.Inject
import javax.inject.Singleton

/*
@Singleton
class HistoryExerciseCacheDataSourceImpl
@Inject
constructor(private val historyExerciseDaoService : HistoryExerciseDaoService)
    : HistoryExerciseCacheDataSource {

    override suspend fun insertHistoryExercise(historyExercise: HistoryExercise): Long = historyExerciseDaoService.insertHistoryExercise(historyExercise)

    override suspend fun updateHistoryExercise(historyExercise: HistoryExercise): Int = historyExerciseDaoService.updateHistoryExercise(historyExercise)

    override suspend fun getHistoryExercise(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<HistoryExercise> = historyExerciseDaoService.getHistoryExercise(query,filterAndOrder,page)

    override suspend fun getHistoryExerciseById(primareyKey: Long): HistoryExercise? = historyExerciseDaoService.getHistoryExerciseById(primareyKey)
}*/
