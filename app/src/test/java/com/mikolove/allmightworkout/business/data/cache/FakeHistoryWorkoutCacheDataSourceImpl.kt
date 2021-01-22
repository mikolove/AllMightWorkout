package com.mikolove.allmightworkout.business.data.cache

import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryWorkoutCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.datasource.database.HISTORY_WORKOUT_PAGINATION_PAGE_SIZE
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

const val FORCE_NEW_HISTORY_WORKOUT_EXCEPTION = "FORCE_NEW_HISTORY_WORKOUT_EXCEPTION"
const val FORCE_SEARCH_HISTORY_WORKOUTS_EXCEPTION = "FORCE_SEARCH_HISTORY_WORKOUTS_EXCEPTION"
const val FORCE_GET_HISTORY_WORKOUT_BY_ID_EXCEPTION = "FORCE_GET_HISTORY_WORKOUT_BY_ID_EXCEPTION"

class FakeHistoryWorkoutCacheDataSourceImpl(
    private val historyWorkoutsData: HashMap<String, HistoryWorkout>,
    private val dateUtil: DateUtil
): HistoryWorkoutCacheDataSource {


    override suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout): Long {
        if(historyWorkout.idHistoryWorkout.equals(FORCE_NEW_HISTORY_WORKOUT_EXCEPTION)){
            throw Exception("Something went wrong inserting the history Workout.")
        }
        if(historyWorkout.idHistoryWorkout.equals(FORCE_GENERAL_FAILURE)){
            return -1 // fail
        }
        historyWorkoutsData.put(historyWorkout.idHistoryWorkout, historyWorkout)
        return 1 // success
    }

    override suspend fun getLastHistoryWorkout(): HistoryWorkout? {

        var lastHistoryWorkout : HistoryWorkout? = null
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH)

        for( historyWorkout in historyWorkoutsData.values){

            if( lastHistoryWorkout == null){
                lastHistoryWorkout = historyWorkout
            }

            val hUpdated_at = LocalDate.parse(historyWorkout.updated_at,dateTimeFormatter)
            val lastHUpdated_at = LocalDate.parse(lastHistoryWorkout?.updated_at,dateTimeFormatter)

            if(hUpdated_at.isAfter(lastHUpdated_at)){
                lastHistoryWorkout = historyWorkout
            }
        }

        return lastHistoryWorkout
    }

    override suspend fun getHistoryWorkouts(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<HistoryWorkout> {
        if(query.equals(FORCE_SEARCH_HISTORY_WORKOUTS_EXCEPTION)){
            throw Exception("Something went wrong searching the cache for history workouts.")
        }
        val results: ArrayList<HistoryWorkout> = ArrayList()
        for(hWorkout in historyWorkoutsData.values){
            if(hWorkout.name.contains(query)){
                results.add(hWorkout)
            }
            if(results.size > (page * HISTORY_WORKOUT_PAGINATION_PAGE_SIZE)){
                break
            }
        }
        return results
    }

    override suspend fun getHistoryWorkoutById(historyWorkoutId: String): HistoryWorkout? {
        if(historyWorkoutId.equals(FORCE_GET_HISTORY_WORKOUT_BY_ID_EXCEPTION)){
            throw Exception("Something went wrong retrieving history workout by id.")
        }
        return historyWorkoutsData[historyWorkoutId]
    }

    override suspend fun getTotalHistoryWorkout(): Int {
        return historyWorkoutsData.size    }


}