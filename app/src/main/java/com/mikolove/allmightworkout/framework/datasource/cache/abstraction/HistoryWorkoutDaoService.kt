package com.mikolove.allmightworkout.framework.datasource.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import com.mikolove.allmightworkout.framework.datasource.cache.database.HISTORY_WORKOUT_PAGINATION_PAGE_SIZE

interface HistoryWorkoutDaoService {

    suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout, idUser : String) : Long

    suspend fun deleteHistoryWorkout(idHistoryWorkout: String) : Int

    suspend fun getHistoryWorkoutById(primaryKey : String) : HistoryWorkout?

    suspend fun getTotalHistoryWorkout(idUser: String) : Int

    suspend fun getHistoryWorkouts(idUser: String) : List<HistoryWorkout>

    suspend fun getHistoryWorkoutOrderByDateDESC(
        query: String,
        page: Int,
        idUser: String
    ): List<HistoryWorkout>

    suspend fun getHistoryWorkoutOrderByDateASC(
        query: String,
        page: Int,
        idUser: String
    ): List<HistoryWorkout>

    suspend fun getHistoryWorkoutOrderByNameDESC(
        query: String,
        page: Int,
        idUser: String
    ): List<HistoryWorkout>

    suspend fun getHistoryWorkoutOrderByNameASC(
        query: String,
        page: Int,
        idUser: String
    ): List<HistoryWorkout>

    suspend fun returnOrderedQuery(
        query: String,
        filterAndOrder: String,
        page: Int,
        idUser: String
    ): List<HistoryWorkout>
}