package com.mikolove.allmightworkout.business.interactors.main.exercise


import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseSetCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.model.ExerciseSetFactory
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class InsertExerciseSet(
    private val exerciseSetCacheDataSource : ExerciseSetCacheDataSource,
    private val exerciseSetNetworkDataSource: ExerciseSetNetworkDataSource,
    private val exerciseSetFactory : ExerciseSetFactory
) {

    fun insertExerciseSet(
        idExerciseSet : String? = null,
        reps : Int,
        weight : Int,
        time : Int,
        restTime : Int,
        idExercise : String,
        stateEvent: StateEvent
    ) : Flow<DataState<ExerciseViewState>?> = flow {

        val newExerciseSet = exerciseSetFactory.createExerciseSet(
            idExerciseSet = idExerciseSet ?: UUID.randomUUID().toString(),
            reps = reps,
            weight = weight,
            time = time,
            restTime = restTime,
            created_at = null
        )

        val cacheResult = safeCacheCall(IO){
            exerciseSetCacheDataSource.insertExerciseSet(exerciseSet = newExerciseSet, idExercise = idExercise)
        }

        val cacheResponse = object :CacheResponseHandler<ExerciseViewState, Long>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Long): DataState<ExerciseViewState>? {
                return if(resultObj>0){

                    val viewState = ExerciseViewState()
                    DataState.data(
                        response = Response(
                            message = INSERT_EXERCISE_SET_SUCCESS,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()
                        ),
                        data = viewState,
                        stateEvent = stateEvent)

                }else{

                    DataState.data(
                        response = Response(
                            message = INSERT_EXERCISE_SET_FAILED,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Error()
                        ),
                        data = null,
                        stateEvent = stateEvent)

                }
            }
        }.getResult()

        emit(cacheResponse)

        updateNetwork(cacheResponse?.stateMessage?.response?.message, newExerciseSet,idExercise)
   }

    private suspend fun updateNetwork(cacheResponse : String?, newExerciseSet : ExerciseSet, idExercise: String){
        if(cacheResponse.equals(INSERT_EXERCISE_SET_SUCCESS)){
            safeApiCall(IO){
                exerciseSetNetworkDataSource.insertExerciseSet(newExerciseSet,idExercise = idExercise)
            }
        }
    }


    companion object{
        val INSERT_EXERCISE_SET_SUCCESS = "Successfully inserted new exercise set."
        val INSERT_EXERCISE_SET_FAILED  = "Failed inserting new exercise set."
    }
}