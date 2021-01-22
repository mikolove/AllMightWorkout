package com.mikolove.allmightworkout.business.data.network

import com.mikolove.allmightworkout.business.data.network.abstraction.HistoryWorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FakeHistoryWorkoutNetworkDataSourceImpl(
    private val historyWorkoutsData : HashMap<String,HistoryWorkout>,
    private val dateUtil : DateUtil
) : HistoryWorkoutNetworkDataSource{

    override suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout) {
        historyWorkoutsData.put(historyWorkout.idHistoryWorkout,historyWorkout)
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

    override suspend fun getHistoryWorkout(): List<HistoryWorkout>? {
        return ArrayList(historyWorkoutsData.values)
    }

    override suspend fun getHistoryWorkoutById(primaryKey: Long): HistoryWorkout? {
        return historyWorkoutsData[primaryKey]
    }
}