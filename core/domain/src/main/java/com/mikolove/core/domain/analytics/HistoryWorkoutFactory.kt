package com.mikolove.core.domain.analytics

import com.mikolove.core.domain.util.DateUtil
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryWorkoutFactory
@Inject
constructor(private val dateUtil: DateUtil) {

    fun createHistoryWorkout(
        idHistoryWorkout: String?,
        name: String?,
        historyExercises : List<HistoryExercise>?,
        started_at : String?,
        ended_at : String?,
        created_at: String?
    ): HistoryWorkout {
        return HistoryWorkout(
            idHistoryWorkout = idHistoryWorkout ?: UUID.randomUUID().toString(),
            name = name ?: "New history workout",
            historyExercises = historyExercises,
            startedAt = started_at ?: dateUtil.getCurrentTimestamp(),
            endedAt = ended_at ?: dateUtil.getCurrentTimestamp(),
            createdAt = created_at ?: dateUtil.getCurrentTimestamp(),
            updatedAt = dateUtil.getCurrentTimestamp())
    }

}