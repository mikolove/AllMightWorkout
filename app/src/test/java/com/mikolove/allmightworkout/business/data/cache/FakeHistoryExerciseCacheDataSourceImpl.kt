package com.mikolove.allmightworkout.business.data.cache

import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryExerciseCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.HistoryExercise
import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import com.mikolove.allmightworkout.business.domain.util.DateUtil

const val FORCE_NEW_HISTORY_EXERCISE_EXCEPTION = "FORCE_NEW_HISTORY_EXERCISE_EXCEPTION"

class FakeHistoryExerciseCacheDataSourceImpl
constructor(
    private val historyWorkoutsData: HashMap<String,HistoryWorkout>,
    private val historyExercisesData: HashMap<String, HistoryExercise>,
    private val dateUtil: DateUtil
): HistoryExerciseCacheDataSource {

    override suspend fun insertHistoryExercise(
        historyExercise: HistoryExercise,
        idHistoryWorkout: String
    ): Long {
        if(historyExercise.idHistoryExercise.equals(FORCE_NEW_HISTORY_EXERCISE_EXCEPTION)){
            throw Exception("Something went wrong inserting the history exercise.")
        }
        if(historyExercise.idHistoryExercise.equals(FORCE_GENERAL_FAILURE)){
            return -1 // fail
        }
        historyExercisesData.put(historyExercise.idHistoryExercise, historyExercise)
        return 1 // success
    }

    override suspend fun getHistoryExercisesByHistoryWorkout(idHistoryWorkout: String): List<HistoryExercise>? {
        return historyWorkoutsData[idHistoryWorkout]?.historyExercises
    }

    override suspend fun getHistoryExerciseById(primaryKey: String): HistoryExercise? {
        return historyExercisesData[primaryKey]
    }

    override suspend fun getTotalHistoryExercise(): Int {
        return historyExercisesData.size
    }
}