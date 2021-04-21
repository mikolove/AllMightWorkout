package com.mikolove.allmightworkout.business.interactors.main.exercise

import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdateNetworkExerciseSets(
    private val exerciseSetNetworkDataSource: ExerciseSetNetworkDataSource
) {

    fun updateNetworkExerciseSets(
        sets : List<ExerciseSet>,
        deletedSets : List<ExerciseSet>,
        idExercise: String,
        stateEvent : StateEvent
    ) : Flow<DataState<ExerciseViewState>?> = flow {

        //Update sets
        safeApiCall(IO){
            exerciseSetNetworkDataSource.updateExerciseSets(sets,idExercise)
        }

        //Update deleted sets
        if(deletedSets?.isNotEmpty()){
            safeApiCall(IO){
                exerciseSetNetworkDataSource.insertDeletedExerciseSets(deletedSets)
            }
        }

        //Just send an execution confirm message since we dont check anything. Could be better
        emit(
            DataState.data<ExerciseViewState>(
                response = Response(
                    message = UPDATE_NETWORK_EXERCISE_SETS_DONE,
                    messageType = MessageType.Success(),
                    uiComponentType = UIComponentType.None()),
                data = null,
                stateEvent = stateEvent
            )
        )
    }

    companion object {
        val UPDATE_NETWORK_EXERCISE_SETS_DONE = "Network update exercise sets has been sended."
    }
}