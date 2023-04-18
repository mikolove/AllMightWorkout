package com.mikolove.allmightworkout.framework.presentation.main.loading

import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.interactors.sync.SyncDeletedExerciseSets
import com.mikolove.allmightworkout.business.interactors.sync.SyncState
import com.mikolove.allmightworkout.business.interactors.sync.SyncWorkoutTypesAndBodyPart
import com.mikolove.allmightworkout.util.printLogD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class TestFlow
constructor(
    private val syncWorkoutTypesAndBodyPart: SyncWorkoutTypesAndBodyPart,
    private val syncDeletedExerciseSets: SyncDeletedExerciseSets,
    private val coroutineScope : CoroutineScope
){

     fun execute() : Flow<DataState<SyncState>> = flow {

        emit(DataState.loading())

        printLogD("TestFlow","Starting test flow")
        printLogD("TestFlow","Launch sync workout type")

        val wkTypesFlow = syncWorkoutTypesAndBodyPart.execute()

         emit(wkTypesFlow)

        printLogD("TestFlow","Launch sync delete exercise set")

        val deleteSetsFlow = syncDeletedExerciseSets.execute()

         emit(deleteSetsFlow)
        printLogD("TestFlow","Everything launched")

    }
}