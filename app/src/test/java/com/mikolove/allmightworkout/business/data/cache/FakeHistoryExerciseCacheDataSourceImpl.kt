package com.mikolove.allmightworkout.business.data.cache

import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryExerciseCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.HistoryExercise
import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import com.mikolove.allmightworkout.business.domain.util.DateUtil

const val FORCE_NEW_HISTORY_EXERCISE_EXCEPTION = "FORCE_NEW_HISTORY_EXERCISE_EXCEPTION"
const val FORCE_UPDATE_HISTORY_EXERCISE_EXCEPTION = "FORCE_UPDATE_HISTORY_EXERCISE_EXCEPTION"

class FakeHistoryExerciseCacheDataSourceImpl
constructor(
    private val historyWorkoutCacheDataSource: HashMap<String,HistoryWorkout>,
    private val historyExercisesData: HashMap<String, HistoryExercise>,
    private val dateUtil: DateUtil
): HistoryExerciseCacheDataSource {

    override suspend fun insertHistoryExercise(historyExercise: HistoryExercise): Long {
        if(historyExercise.idHistoryExercise.equals(FORCE_NEW_HISTORY_EXERCISE_EXCEPTION)){
            throw Exception("Something went wrong inserting the history exercise.")
        }
        if(historyExercise.idHistoryExercise.equals(FORCE_GENERAL_FAILURE)){
            return -1 // fail
        }
        historyExercisesData.put(historyExercise.idHistoryExercise, historyExercise)
        return 1 // success
    }

    override suspend fun updateHistoryExercise(historyExercise: HistoryExercise): Int {
        if(historyExercise.idHistoryExercise.equals(FORCE_UPDATE_HISTORY_EXERCISE_EXCEPTION)){
            throw Exception("Something went wrong updating the history exercise.")
        }
        val updatedHistoryExercise = HistoryExercise(idHistoryExercise = historyExercise.idHistoryExercise,
            name = historyExercise.name,
            bodyPart = historyExercise.bodyPart,
            workoutType = historyExercise.workoutType,
            exerciseType = historyExercise.exerciseType,
            historySets = historyExercise.historySets,
            created_at = historyExercise.created_at,
            started_at = historyExercise.started_at,
            ended_at = historyExercise.ended_at,
            updated_at = dateUtil.getCurrentTimestamp())

        return historyExercisesData.get(historyExercise.idHistoryExercise)?.let {
            historyExercisesData.put(historyExercise.idHistoryExercise, updatedHistoryExercise)
            1 // success
        }?: -1 // nothing to update
    }

    override suspend fun getHistoryExercisesByHistoryWorkout(idHistoryWorkout: String): List<HistoryExercise>? {
        return historyWorkoutCacheDataSource[idHistoryWorkout]?.historyExercises
    }

    override suspend fun getHistoryExerciseById(primaryKey: String): HistoryExercise? {
        return historyExercisesData[primaryKey]
    }

    override suspend fun getTotalHistoryExercise(): Int {
        return historyExercisesData.size
    }
}