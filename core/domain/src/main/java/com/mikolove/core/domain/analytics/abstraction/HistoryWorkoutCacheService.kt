package com.mikolove.core.domain.analytics.abstraction

import com.mikolove.core.domain.analytics.HistoryWorkout

interface HistoryWorkoutCacheService {

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