package com.mikolove.core.interactors.exercise

import com.mikolove.core.domain.exercise.abstraction.ExerciseSetCacheDataSource


class UpdateMultipleExerciseSet(
    private val exerciseSetCacheDataSource: ExerciseSetCacheDataSource,
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