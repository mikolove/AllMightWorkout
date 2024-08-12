package com.mikolove.core.interactors.common

import com.mikolove.core.domain.cache.CacheResponseHandler
import com.mikolove.core.domain.exercise.ExerciseCacheDataSource
import com.mikolove.core.data.util.safeCacheCall
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.allmightworkout.framework.datasource.cache.database.EXERCISE_PAGINATION_PAGE_SIZE
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.MessageType
import com.mikolove.core.domain.state.UIComponentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetExercises(
    val exerciseCacheDataSource: ExerciseCacheDataSource
) {

    fun execute(
        query: String,
        filterAndOrder: String,
        page: Int,
        idUser : String
    ) : Flow<DataState<List<Exercise>>?> = flow{

        emit(DataState.loading())

        var updatedPage = page
        if(page <= 0) updatedPage = 1

        val cacheResult = safeCacheCall(Dispatchers.IO){
            exerciseCacheDataSource.getExercises(
                query = query,
                filterAndOrder = filterAndOrder,
                page = updatedPage,
                idUser = idUser
            )
        }

        val response = object : CacheResponseHandler<List<Exercise>, List<Exercise>>(
            response = cacheResult,
        ){
            override suspend fun handleSuccess(resultObj: List<Exercise>): DataState<List<Exercise>> {
                return when{
                    resultObj.isEmpty() ->{
                        DataState.data(
                            message = GenericMessageInfo.Builder()
                                .id("GetExercises.Success")
                                .title("GetExercises no result")
                                .description(GET_EXERCISES_NO_MATCHING_RESULTS)
                                .uiComponentType(UIComponentType.None)
                                .messageType(MessageType.Success),
                            data = ArrayList(resultObj)
                        )
                    }
                    resultObj.size < (EXERCISE_PAGINATION_PAGE_SIZE * updatedPage) ->{
                        DataState.data(
                            message = GenericMessageInfo.Builder()
                                .id("GetExercises.Success")
                                .title("")
                                .description(GET_EXERCISES_SUCCESS_END)
                                .uiComponentType(UIComponentType.None)
                                .messageType(MessageType.Success),
                            data = ArrayList(resultObj)
                        )
                    }
                    else -> {
                        DataState.data(
                            message = GenericMessageInfo.Builder()
                                .id("GetExercises.Success")
                                .title("")
                                .description(GET_EXERCISES_SUCCESS)
                                .uiComponentType(UIComponentType.None)
                                .messageType(MessageType.Success),
                            data = ArrayList(resultObj)
                        )
                    }
                }
            }
        }.getResult()

        emit(response)

    }

    companion object{
        val GET_EXERCISES_SUCCESS = "Successfully retrieved list of exercises."
        val GET_EXERCISES_SUCCESS_END = "Successfully retrieved list of workouts. No more workouts for this search."
        val GET_EXERCISES_NO_MATCHING_RESULTS = "There are no exercises that match that query."
        val GET_EXERCISES_FAILED = "Failed to retrieve the list of exercises."
    }
}