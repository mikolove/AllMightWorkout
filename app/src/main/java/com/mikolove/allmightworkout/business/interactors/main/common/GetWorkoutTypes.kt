package com.mikolove.allmightworkout.business.interactors.main.common

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutTypeCacheDataSource
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.WorkoutType
import com.mikolove.allmightworkout.business.domain.state.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWorkoutTypes(
    val workoutTypeCacheDataSource: WorkoutTypeCacheDataSource
) {

    fun execute(
        query : String,
        filterAndOrder : String,
        page : Int,
    ): Flow<DataState<List<WorkoutType>>?> = flow {

        var updatedPage = page
        if(page <= 0) updatedPage = 1

        val cacheResult = safeCacheCall(Dispatchers.IO){
            workoutTypeCacheDataSource.getWorkoutTypes(
                query = query,
                filterAndOrder = filterAndOrder,
                page = updatedPage
            )
        }

        val response = object : CacheResponseHandler<List<WorkoutType>, List<WorkoutType>>(
            response = cacheResult,
        ){
            override suspend fun handleSuccess(resultObj: List<WorkoutType>): DataState<List<WorkoutType>>? {

                var message : String = GET_WORKOUTTYPES_SUCCESS
                var uiComponentType : UIComponentType = UIComponentType.None

                if(resultObj.isEmpty()){
                    message = GET_WORKOUTTYPES_NO_MATCHING_RESULTS
                    uiComponentType = UIComponentType.Toast
                }

                return DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("GetWorkoutTypes.Success")
                        .title("")
                        .description(message)
                        .uiComponentType(uiComponentType)
                        .messageType(MessageType.Success)
                    ,
                    data = resultObj
                )
            }
        }.getResult()

        emit(response)
    }

    companion object{
        val GET_WORKOUTTYPES_SUCCESS = "Successfully retrieved list of workoutTypes."
        val GET_WORKOUTTYPES_NO_MATCHING_RESULTS = "There are no workoutTypes that match that query."
        val GET_WORKOUTTYPES_FAILED = "Failed to retrieve the list of workoutTypes."
    }
}