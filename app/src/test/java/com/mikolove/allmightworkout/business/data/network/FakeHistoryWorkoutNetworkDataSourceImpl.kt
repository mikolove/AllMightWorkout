package com.mikolove.allmightworkout.business.data.network

import com.mikolove.core.domain.analytics.HistoryWorkoutNetworkDataSource
import com.mikolove.core.domain.analytics.HistoryWorkout
import com.mikolove.core.domain.util.DateUtil
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FakeHistoryWorkoutNetworkDataSourceImpl(
    private val historyWorkoutsData : HashMap<String, HistoryWorkout>,
    private val dateUtil : DateUtil
) : HistoryWorkoutNetworkDataSource {

    override suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout) {
        historyWorkoutsData.put(historyWorkout.idHistoryWorkout,historyWorkout)
    }

    override suspend fun getLastHistoryWorkouts(): List<HistoryWorkout>? {
        /*var lastHistoryWorkout : HistoryWorkout? = null
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
        }*/

        return ArrayList(historyWorkoutsData.values)
    }

    override suspend fun getHistoryWorkout(): List<HistoryWorkout>? {
        return ArrayList(historyWorkoutsData.values)
    }

    override suspend fun getHistoryWorkoutById(primaryKey: String): HistoryWorkout? {
        return historyWorkoutsData[primaryKey]
    }
}