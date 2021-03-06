package com.mikolove.allmightworkout.business.interactors.main.exercise

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class InsertExercise(
    private val exerciseCacheDataSource: ExerciseCacheDataSource,
    private val exerciseNetworkDataSource: ExerciseNetworkDataSource,
    private val exerciseFactory: ExerciseFactory
) {

    fun insertExercise(
        idExercise: String? = null,
        name: String,
        sets: List<ExerciseSet>?,
        exerciseType: ExerciseType,
        bodyPart: BodyPart,
        stateEvent: StateEvent
    ) : Flow<DataState<ExerciseViewState>?> = flow {

        val newExercise = exerciseFactory.createExercise(
            idExercise = idExercise ?: UUID.randomUUID().toString(),
            name = name,
            sets = sets,
            bodyPart = bodyPart,
            exerciseType = exerciseType,
            created_at = null
        )

        val cacheResult = safeCacheCall(IO){
            exerciseCacheDataSource.insertExercise(newExercise)
        }

        val response = object : CacheResponseHandler<ExerciseViewState,Long>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Long): DataState<ExerciseViewState>? {
                return if(resultObj >0 ){

                    val viewState = ExerciseViewState(exerciseSelected = newExercise, isExistExercise = true)
                    DataState.data(
                        response = Response(
                            message = INSERT_EXERCISE_SUCCESS,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()
                        ),
                        data = viewState,
                        stateEvent = stateEvent
                    )

                }else{

                    DataState.data(
                        response = Response(
                            message = INSERT_EXERCISE_FAILED,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Error()
                        ),
                        data = null,
                        stateEvent = stateEvent)
                }
            }
        }.getResult()

        emit(response)

        updateNetwork(response?.stateMessage?.response?.message, newExercise)

    }

    suspend fun updateNetwork(cacheResponse : String?, exercise : Exercise){
        if(cacheResponse.equals(INSERT_EXERCISE_SUCCESS)){
            safeApiCall(IO){
                exerciseNetworkDataSource.insertExercise(exercise)
            }
        }
    }

    companion object{

        val INSERT_EXERCISE_SUCCESS = "Successfully inserted new exercise."
        val INSERT_EXERCISE_FAILED  = "Failed inserting new exercise."
    }
}