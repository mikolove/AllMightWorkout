package com.mikolove.allmightworkout.business.data.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.HistoryExerciseSet

interface HistoryExerciseSetNetworkDataSource {

    suspend fun insertHistoryExerciseSet(
        historyExerciseSet: HistoryExerciseSet,
        historyExerciseId: String,
        historyWorkoutId: String
    )

    suspend fun getHistoryExerciseSetsByHistoryExerciseId(
        idHistoryExerciseId: String,
        idHistoryWorkout: String
    ) : List<HistoryExerciseSet>

}