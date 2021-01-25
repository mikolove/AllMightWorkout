
package com.mikolove.allmightworkout.business.data.network.implementation

import com.mikolove.allmightworkout.business.data.network.abstraction.HistoryExerciseSetNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.HistoryExerciseSet
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.HistoryExerciseSetFirestoreService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryExerciseSetNetworkDataSourceImpl
@Inject
constructor(private val historyExerciseSetFirestoreService : HistoryExerciseSetFirestoreService) : HistoryExerciseSetNetworkDataSource{

    override suspend fun insertHistoryExerciseSet(historyExerciseSet: HistoryExerciseSet, historyExerciseId: String) = historyExerciseSetFirestoreService.insertHistoryExerciseSet(historyExerciseSet,historyExerciseId)

    override suspend fun updateHistoryExerciseSet(historyExerciseSet: HistoryExerciseSet)  = historyExerciseSetFirestoreService.updateHistoryExerciseSet(historyExerciseSet)

    override suspend fun getHistoryExerciseSetsByHistoryExerciseId(idHistoryExerciseId: Long): List<HistoryExerciseSet> = historyExerciseSetFirestoreService.getHistoryExerciseSetsByHistoryExerciseId(idHistoryExerciseId)
}
