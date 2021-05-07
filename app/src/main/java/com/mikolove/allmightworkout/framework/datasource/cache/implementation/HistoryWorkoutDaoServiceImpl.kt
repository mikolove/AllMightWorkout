package com.mikolove.allmightworkout.framework.datasource.cache.implementation

import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.HistoryWorkoutDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.database.HistoryWorkoutDao
import com.mikolove.allmightworkout.framework.datasource.cache.database.returnOrderedQuery
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.HistoryWorkoutCacheMapper
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.HistoryWorkoutWithExercisesCacheMapper

class HistoryWorkoutDaoServiceImpl
constructor(
    private val historyWorkoutDao : HistoryWorkoutDao,
    private val historyWorkoutCacheMapper: HistoryWorkoutCacheMapper,
    private val historyWorkoutWithExercisesCacheMapper: HistoryWorkoutWithExercisesCacheMapper
) : HistoryWorkoutDaoService{

    override suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout): Long {
        return historyWorkoutDao.insertHistoryWorkout(historyWorkoutCacheMapper.mapToEntity(historyWorkout))
    }

    override suspend fun deleteHistoryWorkout(idHistoryWorkout: String): Int {
        return historyWorkoutDao.deleteHistoryWorkout(idHistoryWorkout)
    }

    override suspend fun getHistoryWorkoutById(primaryKey: String): HistoryWorkout? {
        return historyWorkoutDao.getHistoryWorkoutById(primaryKey)?.let {
            historyWorkoutWithExercisesCacheMapper.mapFromEntity(it)
        }
    }

    override suspend fun getTotalHistoryWorkout(): Int {
        return historyWorkoutDao.getTotalHistoryWorkout()
    }

    override suspend fun getHistoryWorkouts(): List<HistoryWorkout> {
        return historyWorkoutWithExercisesCacheMapper.entityListToDomainList(
            historyWorkoutDao.getHistoryWorkouts()
        )
    }

    override suspend fun getHistoryWorkoutOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<HistoryWorkout> {
        return historyWorkoutWithExercisesCacheMapper.entityListToDomainList(
            historyWorkoutDao.getHistoryWorkoutOrderByDateDESC(query, page)
        )
    }

    override suspend fun getHistoryWorkoutOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<HistoryWorkout> {
        return historyWorkoutWithExercisesCacheMapper.entityListToDomainList(
            historyWorkoutDao.getHistoryWorkoutOrderByDateASC(query, page)
        )
    }

    override suspend fun getHistoryWorkoutOrderByNameDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<HistoryWorkout> {
        return historyWorkoutWithExercisesCacheMapper.entityListToDomainList(
            historyWorkoutDao.getHistoryWorkoutOrderByNameDESC(query, page)
        )
    }

    override suspend fun getHistoryWorkoutOrderByNameASC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<HistoryWorkout> {
        return historyWorkoutWithExercisesCacheMapper.entityListToDomainList(
            historyWorkoutDao.getHistoryWorkoutOrderByNameASC(query, page)
        )
    }

    override suspend fun returnOrderedQuery(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<HistoryWorkout> {
        return historyWorkoutWithExercisesCacheMapper.entityListToDomainList(
            historyWorkoutDao.returnOrderedQuery(query, filterAndOrder, page)
        )
    }
}