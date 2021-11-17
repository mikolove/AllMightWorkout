package com.mikolove.allmightworkout.business.interactors.main.exercise

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseSetCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.state.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdateMultipleExerciseSet(
    private val exerciseSetCacheDataSource: ExerciseSetCacheDataSource,
    private val exerciseSetNetworkDataSource: ExerciseSetNetworkDataSource
) {

   /* private var onUpdateError: Boolean = false

    fun updateMultipleExerciseSet(
        sets: List<ExerciseSet>,
        idExercise: String,
        stateEvent: StateEvent
    ): Flow<DataState<ExerciseViewState>?> = flow {

        val successfulUpdate: ArrayList<ExerciseSet> = ArrayList()

        for (set in sets) {

            val cacheResult = safeCacheCall(Dispatchers.IO) {
                exerciseSetCacheDataSource.updateExerciseSet(
                    primaryKey = set.idExerciseSet,
                    reps = set.reps,
                    weight = set.weight,
                    time = set.time,
                    restTime = set.restTime,
                    order = set.order,
                    updatedAt = set.updatedAt,
                    idExercise = idExercise
                )
            }

            val response = object : CacheResponseHandler<ExerciseViewState, Int>(
                response = cacheResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: Int): DataState<ExerciseViewState>? {
                    if (resultObj < 0) {
                        onUpdateError = true
                    } else {
                        successfulUpdate.add(set)
                    }
                    return null
                }
            }.getResult()

            if (response?.message?.response?.message?.contains(stateEvent.errorInfo()) == true) {
                onUpdateError = true
            }
        }

        if (onUpdateError) {
            emit(
                DataState.data<ExerciseViewState>(
                    response = Response(
                        message = UPDATE_EXERCISE_SETS_ERRORS,
                        uiComponentType = UIComponentType.None(),
                        messageType = MessageType.Error(),
                    ),
                    data = null,
                    stateEvent = stateEvent
                )
            )
        } else {
            emit(
                DataState.data<ExerciseViewState>(
                    response = Response(
                        message = UPDATE_EXERCISE_SETS_SUCCESS,
                        uiComponentType = UIComponentType.None(),
                        messageType = MessageType.Success(),
                    ),
                    data = null,
                    stateEvent = stateEvent
                )
            )
        }

        //updateNetwork(successfulUpdate, idExercise)
    }


    private suspend fun updateNetwork(
        successfulUpdate: ArrayList<ExerciseSet>,
        idExercise: String
    ) {
        for (set in successfulUpdate) {
            safeApiCall(Dispatchers.IO) {
                exerciseSetNetworkDataSource.updateExerciseSet(set, idExercise)
            }
        }
    }*/

    companion object {
        val UPDATE_EXERCISE_SETS_SUCCESS = "Successfully updated exercise sets."
        val UPDATE_EXERCISE_SETS_ERRORS = "Not all the exercise sets were updated. Errors occurs."
    }

}