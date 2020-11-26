package com.mikolove.allmightworkout.business.domain.model

import com.mikolove.allmightworkout.business.domain.util.DateUtil
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class HistoryWorkoutFactory
@Inject
constructor(private val historyExerciseFactory: HistoryExerciseFactory) {


    fun createHistoryWorkoutFromWorkout(
        idHistoryWorkout : String?,
        workout: Workout)
    : HistoryWorkout{

        val listOfHistoryExercise = ArrayList<HistoryExercise>()
        workout.exercises?.let { listOfExercise ->
            for( i in 1..listOfExercise.size){
                listOfHistoryExercise.add(
                    historyExerciseFactory.createHistoryExerciseFromExercise(
                                            idHistoryExercise = null,
                                            exercise = listOfExercise[i])
                )
            }
        }

        return HistoryWorkout(
            idHistoryWorkout = idHistoryWorkout ?: UUID.randomUUID().toString(),
            name = workout.name,
            historyExercises = listOfHistoryExercise,
            created_at = workout.created_at,
            updated_at = workout.updated_at)
    }

}