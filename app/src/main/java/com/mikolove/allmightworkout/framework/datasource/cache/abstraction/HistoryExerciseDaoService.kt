package com.mikolove.allmightworkout.framework.datasource.cache.abstraction

import com.mikolove.allmightEXERCISE.framework.datasource.database.HISTORY_EXERCISE_PAGINATION_PAGE_SIZE
import com.mikolove.allmightworkout.business.domain.model.HistoryExercise

interface HistoryExerciseDaoService {

    suspend fun insertHistoryExercise(historyExercise: HistoryExercise) : Long

    suspend fun updateHistoryExercise(historyExercise: HistoryExercise) : Int

    suspend fun getHistoryExercises() : List<HistoryExercise>

    suspend fun getHistoryExerciseById(primaryKey : String) : HistoryExercise?

    suspend fun getTotalHistoryExercise() : Int

    suspend fun getHistoryExerciseOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int = HISTORY_EXERCISE_PAGINATION_PAGE_SIZE
    ): List<HistoryExercise>

    suspend fun getHistoryExerciseOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int = HISTORY_EXERCISE_PAGINATION_PAGE_SIZE
    ): List<HistoryExercise>

    suspend fun getHistoryExerciseOrderByNameDESC(
        query: String,
        page: Int,
        pageSize: Int = HISTORY_EXERCISE_PAGINATION_PAGE_SIZE
    ): List<HistoryExercise>

    suspend fun getHistoryExerciseOrderByNameASC(
        query: String,
        page: Int,
        pageSize: Int = HISTORY_EXERCISE_PAGINATION_PAGE_SIZE
    ): List<HistoryExercise>

    suspend fun returnOrderedQuery(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<HistoryExercise>
}