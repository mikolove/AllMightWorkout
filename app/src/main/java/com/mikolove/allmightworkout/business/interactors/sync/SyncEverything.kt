package com.mikolove.allmightworkout.business.interactors.sync

import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.MessageType
import com.mikolove.allmightworkout.business.domain.state.UIComponentType
import com.mikolove.allmightworkout.util.printLogD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SyncEverything
constructor(
    private val syncWorkoutTypesAndBodyPart: SyncWorkoutTypesAndBodyPart,
    private val syncDeletedExerciseSets: SyncDeletedExerciseSets,
    private val syncDeletedExercises: SyncDeletedExercises,
    private val syncDeletedWorkouts: SyncDeletedWorkouts,
    private val syncHistory: SyncHistory,
    private val syncExercises: SyncExercises,
    private val syncWorkouts : SyncWorkouts,
    private val syncWorkoutExercises: SyncWorkoutExercises
) {

    private var errorOccured : Boolean = false

    operator fun invoke(idUser : String) : Flow<DataState<SyncState>> = flow {

        val listOfSync = mapOf(
            0 to "workoutandbp",
            1 to "deletedsets",
            2 to "deletedexercises",
            3 to "deletedworkouts",
            4 to "history",
            5 to "exercises",
            6 to "workouts",
            7 to "workoutexercises"
        )

        emit(DataState.loading())

        for ( (idSync, sync) in listOfSync.toSortedMap()){

            printLogD("SyncEverything","Process list ${idSync} - ${sync}")
            val dataState : DataState<SyncState> = when(sync){
                "workoutandbp" ->  syncWorkoutTypesAndBodyPart.execute()
                "deletedsets" ->   syncDeletedExerciseSets.execute()
                "deletedexercises" -> syncDeletedExercises.execute()
                "deletedworkouts" -> syncDeletedWorkouts.execute()
                "history" -> syncHistory.execute(idUser = idUser)
                "exercises" -> syncExercises.execute(idUser = idUser)
                "workouts" -> syncWorkouts.execute(idUser = idUser)
                "workoutexercises" -> syncWorkoutExercises.execute(idUser = idUser)
                else -> {
                    DataState.data<SyncState>(
                        message = GenericMessageInfo.Builder()
                            .id("SyncEverything.Error")
                            .title(SYNC_EV_ERROR_TITLE)
                            .description(SYNC_EV_ERROR_DESCRIPTION)
                            .messageType(MessageType.Error)
                            .uiComponentType(UIComponentType.None),
                        data = SyncState.FAILURE
                    )
                }
            }

            //emit(dataState)
            printLogD("SyncEverything","${dataState.message?.title}  - ${dataState.message?.description} - ${dataState.data}")
            if(dataState.data == SyncState.FAILURE){
                errorOccured = true
                break
            }
        }

        if(!errorOccured){
            emit(
                DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("SyncEverything.Success")
                        .title(SYNC_EV_SUCCESS_TITLE)
                        .description(SYNC_EV_SUCCESS_DESCRIPTION)
                        .messageType(MessageType.Success)
                        .uiComponentType(UIComponentType.None),
                    data = SyncState.SUCCESS
                )
            )
        }else{
            emit(
                DataState.error(
                    message = GenericMessageInfo.Builder()
                        .id("SyncEverything.Error")
                        .title(SYNC_GERROR_TITLE)
                        .description(SYNC_GERROR_DESCRIPTION)
                        .messageType(MessageType.Error)
                        .uiComponentType(UIComponentType.Toast),
                ))
        }

    }

    companion object{

        val SYNC_GERROR_TITLE = "Sync everything global error"
        val SYNC_GERROR_DESCRIPTION = "Errors occured during sync. Check internet or try again later."

        val SYNC_EV_ERROR_TITLE = "Sync everything error"
        val SYNC_EV_ERROR_DESCRIPTION = "Error occured during sync everything. Executed item not in the list."

        val SYNC_EV_SUCCESS_TITLE = "Sync everything success"
        val SYNC_EV_SUCCESS_DESCRIPTION = "Everything synced go to app"

    }
}