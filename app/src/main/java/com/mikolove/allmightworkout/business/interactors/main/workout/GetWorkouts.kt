package com.mikolove.allmightworkout.business.interactors.main.workout

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.framework.datasource.cache.database.WORKOUT_PAGINATION_PAGE_SIZE
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWorkouts(
    private val workoutCacheDataSource: WorkoutCacheDataSource
) {

    fun execute(
        query: String,
        filterAndOrder: String,
        page: Int
    ) : Flow<DataState<List<Workout>>?>  = flow{

        emit(DataState.loading())

        var updatedPage = page
        if(page <= 0) updatedPage = 1

        val cacheResult = safeCacheCall(IO){
            workoutCacheDataSource.getWorkouts(
                query = query,
                filterAndOrder = filterAndOrder,
                page = updatedPage
            )
        }

        val response = object : CacheResponseHandler<List<Workout>,List<Workout>>(
            response = cacheResult
        ){
            override suspend fun handleSuccess(resultObj: List<Workout>): DataState<List<Workout>> {
                return when{
                    resultObj.isEmpty() ->{
                        DataState.data(
                            message = GenericMessageInfo.Builder()
                                .id("GetWorkouts.Success")
                                .title("GetWorkouts no result")
                                .description(GET_WORKOUTS_NO_MATCHING_RESULTS)
                                .uiComponentType(UIComponentType.None)
                                .messageType(MessageType.Success),
                            data = ArrayList(resultObj)
                        )
                    }
                   resultObj.size < (WORKOUT_PAGINATION_PAGE_SIZE * updatedPage) ->{
                       DataState.data(
                           message = GenericMessageInfo.Builder()
                               .id("GetWorkouts.Success")
                               .title("")
                               .description(GET_WORKOUTS_SUCCESS_END)
                               .uiComponentType(UIComponentType.None)
                               .messageType(MessageType.Success),
                           data = ArrayList(resultObj)
                       )
                    }
                    else -> {
                        DataState.data(
                            message = GenericMessageInfo.Builder()
                                .id("GetWorkouts.Success")
                                .title("")
                                .description(GET_WORKOUTS_SUCCESS)
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
        val GET_WORKOUTS_SUCCESS = "Successfully retrieved list of workouts."
        val GET_WORKOUTS_SUCCESS_END = "Successfully retrieved list of workouts. No more workouts for this search."
        val GET_WORKOUTS_NO_MATCHING_RESULTS = "There are no workouts that match that query."
        val GET_WORKOUTS_FAILED = "Failed to retrieve the list of workouts."
    }
}
