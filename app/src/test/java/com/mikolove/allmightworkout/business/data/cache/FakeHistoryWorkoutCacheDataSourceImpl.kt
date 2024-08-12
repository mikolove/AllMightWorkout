package com.mikolove.allmightworkout.business.data.cache

import com.mikolove.core.domain.analytics.HistoryWorkoutCacheDataSource
import com.mikolove.core.domain.analytics.HistoryWorkout
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.datasource.cache.database.HISTORY_WORKOUT_PAGINATION_PAGE_SIZE
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

    override suspend fun deleteHistoryWorkout(idHistoryWorkout: String): Int {
        return historyWorkoutsData.remove(idHistoryWorkout)?.let{
            1
        }?: -1
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