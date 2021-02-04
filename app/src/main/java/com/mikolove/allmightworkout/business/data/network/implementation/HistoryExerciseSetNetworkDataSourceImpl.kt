
package com.mikolove.allmightworkout.business.data.network.implementation

import com.mikolove.allmightworkout.business.data.network.abstraction.HistoryExerciseSetNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.HistoryExerciseSet
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.HistoryExerciseSetFirestoreService
import javax.inject.Inject
import javax.inject.Singleton

class HistoryExerciseSetNetworkDataSourceImpl
constructor(private val historyExerciseSetFirestoreService : HistoryExerciseSetFirestoreService) : HistoryExerciseSetNetworkDataSource{

    override suspend fun insertHistoryExerciseSet(
        historyExerciseSet: HistoryExerciseSet,
        historyExerciseId: String,
        historyWorkoutId: String
    ) = historyExerciseSetFirestoreService.insertHistoryExerciseSet(
        historyExerciseSet,
        historyExerciseId,
        historyWorkoutId
    )

    override suspend fun getHistoryExerciseSetsByHistoryExerciseId(
        idHistoryExerciseId: String,
        idHistoryWorkout: String
    ): List<HistoryExerciseSet> = historyExerciseSetFirestoreService.getHistoryExerciseSetsByHistoryExerciseId(
        idHistoryExerciseId,
        idHistoryWorkout
    )
}
