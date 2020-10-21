package com.mikolove.allmightworkout.business.domain.model

import com.mikolove.allmightworkout.business.domain.util.DateUtil
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryWorkoutFactory
@Inject
constructor(private val dateUtil: DateUtil, private val historyExerciseFactory: HistoryExerciseFactory) {

    fun createHistoryFactory(idHistoryWorkout : Long?,
                          workout: Workout
    ) : HistoryWorkout{

        //Generate History set from exercise
        val listOfHistoryExercise = ArrayList<HistoryExercise>()
        workout.exercises?.let { listOfExercise ->
            for( i in 1..listOfExercise.size){
                listOfHistoryExercise.add(
                    historyExerciseFactory.createHistoryExercise(
                        idHistoryExercise = i.toLong(),
                        exercise = listOfExercise[i]
                    )
                )
            }
        }

        return HistoryWorkout(
            idHistoryWorkout = idHistoryWorkout ?: 0L,
            workout = workout,
            historyExercises = listOfHistoryExercise,
            created_at = dateUtil.getCurrentTimestamp(),
            updated_at = dateUtil.getCurrentTimestamp())
    }

}