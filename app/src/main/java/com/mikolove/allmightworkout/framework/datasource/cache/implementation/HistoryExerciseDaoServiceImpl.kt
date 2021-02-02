package com.mikolove.allmightworkout.framework.datasource.cache.implementation

import com.mikolove.allmightEXERCISE.framework.datasource.database.HistoryExerciseDao
import com.mikolove.allmightworkout.business.domain.model.HistoryExercise
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.HistoryExerciseDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.HistoryExerciseCacheMapper
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.HistoryExerciseWithSetsCacheMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryExerciseDaoServiceImpl
@Inject
constructor(
    private val historyExerciseDao : HistoryExerciseDao,
    private val historyExerciseCacheMapper : HistoryExerciseCacheMapper,
    private val historyExerciseWithSetsCacheMapper: HistoryExerciseWithSetsCacheMapper
) : HistoryExerciseDaoService{

    override suspend fun insertHistoryExercise(historyExercise: HistoryExercise): Long {
        return historyExerciseDao.insertHistoryExercise(historyExerciseCacheMapper.mapToEntity(historyExercise))
    }

    override suspend fun getHistoryExercisesByHistoryWorkout(idHistoryWorkout: String): List<HistoryExercise>? {
        return historyExerciseDao.getHistoryExercisesByHistoryWorkout(idHistoryWorkout)?.let {
            historyExerciseWithSetsCacheMapper.entityListToDomainList(it)
        }
    }

    override suspend fun getHistoryExerciseById(primaryKey: String): HistoryExercise? {
        return historyExerciseDao.getHistoryExerciseById(primaryKey)?.let {
            historyExerciseWithSetsCacheMapper.mapFromEntity(it)
        }
    }

    override suspend fun getTotalHistoryExercise(): Int {
        return historyExerciseDao.getTotalHistoryExercise()
    }
}