package com.mikolove.allmightworkout.business.data.cache

import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryExerciseSetCacheDataSource
import com.mikolove.core.domain.analytics.HistoryExercise
import com.mikolove.core.domain.analytics.HistoryExerciseSet

const val FORCE_NEW_HISTORY_EXERCISE_SET_EXCEPTION = "FORCE_NEW_HISTORY_EXERCISE_SET_EXCEPTION"

class FakeHistoryExerciseSetCacheDataSourceImpl
constructor(
    private val historyExercisesData: HashMap<String, HistoryExercise>,
    private val historyExerciseSetsData: HashMap<String, HistoryExerciseSet>
) : HistoryExerciseSetCacheDataSource{

    override suspend fun insertHistoryExerciseSet(
        historyExerciseSet: HistoryExerciseSet,
        historyExerciseId: String
    ): Long {
        if(historyExerciseSet.idHistoryExerciseSet.equals(FORCE_NEW_HISTORY_EXERCISE_SET_EXCEPTION)){
            throw Exception("Something went wrong inserting the history exercise.")
        }
        if(historyExerciseSet.idHistoryExerciseSet.equals(FORCE_GENERAL_FAILURE)){
            return -1 // fail
        }
        historyExerciseSetsData.put(historyExerciseSet.idHistoryExerciseSet, historyExerciseSet)
        return 1 // success
    }

    override suspend fun getHistoryExerciseSetsByHistoryExercise(idHistoryExercise: String): List<HistoryExerciseSet>? {
        return historyExercisesData[idHistoryExercise]?.historySets
    }

    override suspend fun getHistoryExerciseSetById(idHistoryExerciseSet: String): HistoryExerciseSet? {
        return historyExerciseSetsData[idHistoryExerciseSet]
    }

    override suspend fun getTotalHistoryExerciseSet(idHistoryExercise: String): Int {
        return historyExerciseSetsData.size
    }
}