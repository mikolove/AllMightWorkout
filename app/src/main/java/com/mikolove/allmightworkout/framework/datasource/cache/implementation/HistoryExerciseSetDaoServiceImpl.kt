package com.mikolove.allmightworkout.framework.datasource.cache.implementation

import com.mikolove.allmightworkout.business.domain.model.HistoryExerciseSet
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.HistoryExerciseSetDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.database.HistoryExerciseSetDao
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.HistoryExerciseSetCacheMapper
import javax.inject.Inject
import javax.inject.Singleton

class HistoryExerciseSetDaoServiceImpl
constructor(
    private val historyExerciseSetDao : HistoryExerciseSetDao,
    private val historyExerciseSetCacheMapper: HistoryExerciseSetCacheMapper
) : HistoryExerciseSetDaoService{

    override suspend fun insertHistoryExerciseSet(
        historyExerciseSet: HistoryExerciseSet,
        historyExerciseId: String
    ): Long {
        val historyExerciseSetCacheEntity = historyExerciseSetCacheMapper.mapToEntity(historyExerciseSet)
        historyExerciseSetCacheEntity.idHistoryExercise = historyExerciseId
        return historyExerciseSetDao.insertHistoryExerciseSet(historyExerciseSetCacheEntity)
    }

    override suspend fun getHistoryExerciseSetById(idHistoryExerciseSet: String): HistoryExerciseSet? {
        return historyExerciseSetDao.getHistoryExerciseSetById(idHistoryExerciseSet)?.let {
            historyExerciseSetCacheMapper.mapFromEntity(it)
        }
    }

    override suspend fun getHistoryExerciseSetsByHistoryExercise(idHistoryExercise: String): List<HistoryExerciseSet> {
        return historyExerciseSetCacheMapper.entityListToDomainList(
            historyExerciseSetDao.getHistoryExerciseSetsByHistoryExercise(idHistoryExercise)
        )
    }

    override suspend fun getTotalHistoryExerciseSet(): Int {
        return historyExerciseSetDao.getTotalHistoryExerciseSet()
    }
}