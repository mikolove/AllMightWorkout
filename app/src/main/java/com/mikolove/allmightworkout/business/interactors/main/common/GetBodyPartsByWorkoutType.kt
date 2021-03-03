package com.mikolove.allmightworkout.business.interactors.main.common

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.BodyPartCacheDataSource
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.business.interactors.main.workout.GetWorkouts
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseViewState
import com.mikolove.allmightworkout.framework.presentation.main.workout.state.WorkoutViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetBodyPartsByWorkoutType
    (val bodyPartCacheDataSource: BodyPartCacheDataSource) {

        inline fun <reified ViewState >getBodyPartsByWorkoutType(
            idWorkoutType : String,
            stateEvent : StateEvent
        ) : Flow<DataState<ViewState>?> =  flow {


            val cacheResult = safeCacheCall(IO){
                bodyPartCacheDataSource.getBodyPartsByWorkoutType(idWorkoutType)
            }

            val response = object : CacheResponseHandler<ViewState, List<BodyPart>>(
                response = cacheResult,
                stateEvent = stateEvent
            ){
                override suspend fun handleSuccess(resultObj: List<BodyPart>): DataState<ViewState>? {

                    val viewState = when(ViewState::class) {
                        WorkoutViewState::class -> WorkoutViewState(listBodyPartsByWorkoutTypes = ArrayList(resultObj))
                        //ExerciseViewState::class -> ExerciseViewState(listBodyPartsByWorkoutTypes = ArrayList(resultObj))
                        else -> null
                    }

                    var message : String? = GET_BODYPART_BY_WORKOUT_TYPES_SUCCESS
                    var uiComponentType : UIComponentType = UIComponentType.None()

                    if(resultObj.size == 0){
                        message = GET_BODYPART_BY_WORKOUT_TYPES_NO_RESULT
                        uiComponentType = UIComponentType.None()
                    }

                    return DataState.data(
                        response = Response(
                            message = GET_BODYPART_BY_WORKOUT_TYPES_SUCCESS,
                            uiComponentType = UIComponentType.None(),
                            messageType = MessageType.Success()
                        ),
                        data = viewState as ViewState,
                        stateEvent = stateEvent
                    )
                }
            }.getResult()

            emit(response)
        }

    companion object{
        val GET_BODYPART_BY_WORKOUT_TYPES_SUCCESS = "Successfully retrieved the bodyparts from the cache."
        val GET_BODYPART_BY_WORKOUT_TYPES_NO_RESULT = "Successfully retrieved the bodyparts from the cache."
        val GET_BODYPART_BY_WORKOUT_TYPES_FAILED = "Failed to retrieved the bodyparts from the cache."
    }
}