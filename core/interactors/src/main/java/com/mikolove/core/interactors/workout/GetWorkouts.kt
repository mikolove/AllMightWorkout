package com.mikolove.core.interactors.workout

import com.mikolove.core.domain.state.DataState
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.MessageType
import com.mikolove.core.domain.state.UIComponentType
import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.domain.workout.abstraction.WorkoutCacheDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform

/*class GetWorkouts(
    private val workoutCacheDataSource: WorkoutCacheDataSource
) {

    fun execute(
        idUser : String,
    ) : Flow<DataState<List<Workout>>?>  =
        workoutCacheDataSource.getWorkouts(idUser)
        .transform<List<Workout>, DataState<List<Workout>>?> { listWorkouts ->
            if(listWorkouts.isEmpty()){
                emit(
                    DataState.data(
                        message = GenericMessageInfo.Builder()
                            .id("GetWorkouts.NoMatchingResults")
                            .title("GetWorkouts no result")
                            .description(GET_WORKOUTS_NO_MATCHING_RESULTS)
                            .uiComponentType(UIComponentType.None)
                            .messageType(MessageType.Success),
                        data = listWorkouts
                    )
                )
            }else{
                emit(
                    DataState.data(
                        message = GenericMessageInfo.Builder()
                            .id("GetWorkouts.Success")
                            .title("GetWorkouts success")
                            .description(GET_WORKOUTS_SUCCESS)
                            .uiComponentType(UIComponentType.None)
                            .messageType(MessageType.Success),
                        data = listWorkouts
                    )
                )
            }
        }.onStart { emit(DataState.loading()) }

    companion object{
        val GET_WORKOUTS_SUCCESS = "Successfully retrieved list of workouts."
        val GET_WORKOUTS_NO_MATCHING_RESULTS = "There are no workouts that match that query."
    }
}
*/