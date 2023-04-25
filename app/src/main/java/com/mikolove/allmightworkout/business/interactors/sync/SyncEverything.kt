package com.mikolove.allmightworkout.business.interactors.sync

import com.mikolove.allmightworkout.business.domain.state.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SyncEverything
constructor(
    private val syncWorkoutTypesAndBodyPart: SyncWorkoutTypesAndBodyPart,
    private val syncDeletedExerciseSets: SyncDeletedExerciseSets,
    private val syncDeletedExercises: SyncDeletedExercises
) {

    fun execute() : Flow<DataState<SyncState>> = flow {

        val listOfSync = listOf(
            "workoutandbp",
            "deletedSets",
            "deletedexercises",
            "deletedWorkouts",
            "history",
            "exercises",
            "workouts",
            "workoutexercises"

        )

        emit(DataState.loading())

        for (sync in listOfSync){

            val dataState : DataState<SyncState> = when(sync){
                "workoutandbp" ->  syncWorkoutTypesAndBodyPart.execute()
                "deletedSets" ->   syncDeletedExerciseSets.execute()
                "deletedexercises" -> syncDeletedExercises.execute()
                "deletedWorkouts" ->
                "history" ->
                "exercises" ->
                "workouts" ->
                "workoutexercises" ->
            }

            emit(dataState)
            if(dataState.data == SyncState.FAILURE)
                break
        }
    }

    companion object{

    }
}