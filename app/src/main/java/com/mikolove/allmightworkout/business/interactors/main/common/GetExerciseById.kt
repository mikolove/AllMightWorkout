package com.mikolove.allmightworkout.business.interactors.main.common

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.state.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetExerciseById
    (val exerciseCacheDataSource: ExerciseCacheDataSource){

     /*   inline fun <reified ViewState> getExerciseById(
            idExercise : String,
            stateEvent : StateEvent
        ) : Flow<DataState<ViewState>?> = flow {

            val cacheResult = safeCacheCall(IO){
                exerciseCacheDataSource.getExerciseById(idExercise)
            }

            val response  = object : CacheResponseHandler<ViewState, Exercise>(
                response = cacheResult,
                stateEvent = stateEvent
            ){
                override suspend fun handleSuccess(resultObj: Exercise): DataState<ViewState>? {

                    val viewState = ExerciseViewState(exerciseSelected = resultObj)

                    return DataState.data(
                        response = Response(
                            message= GET_EXERCISE_BY_ID_SUCCESS,
                            uiComponentType = UIComponentType.None(),
                            messageType = MessageType.Success(),
                        ),
                        data = viewState as ViewState,
                        stateEvent = stateEvent
                    )
                }
            }.getResult()

            emit(response)
        }
*/
    companion object{
        val GET_EXERCISE_BY_ID_SUCCESS = "Successfully retrieved exercise by id."
    }
}