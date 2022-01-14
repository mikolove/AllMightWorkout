package com.mikolove.allmightworkout.business.interactors.main.exercise


import com.mikolove.allmightworkout.business.data.datastore.AppDataStore
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.MessageType
import com.mikolove.allmightworkout.business.domain.state.UIComponentType
import com.mikolove.allmightworkout.framework.presentation.common.DataStoreKeys
import com.mikolove.allmightworkout.framework.presentation.main.exercise_list.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetExerciseOrderAndFilter(private val appDataStoreManager : AppDataStore
) {
    fun execute() : Flow<DataState<ExerciseOrderAndFilter>> = flow {

        val filter = appDataStoreManager.readValue(DataStoreKeys.EXERCISE_FILTER)?.let { filter ->
            getFilterFromValue(filter)
        }?: getFilterFromValue(ExerciseFilterOptions.FILTER_NAME.value)
        val order = appDataStoreManager.readValue(DataStoreKeys.EXERCISE_ORDER)?.let { order ->
            getOrderFromValue(order)
        }?: getOrderFromValue(ExerciseOrderOptions.DESC.value)
        emit(
            DataState.data(
            message = GenericMessageInfo.Builder()
                .id("GetExerciseOrderAndFilter.Success")
                .title("")
                .description(GET_EXERCISE_FILTER_AND_ORDER_SUCCESS)
                .messageType(MessageType.Success)
                .uiComponentType(UIComponentType.None),
            data = ExerciseOrderAndFilter(order = order, filter = filter)
        ))
    }.catch { e ->
        emit(
            DataState.error(
            message = GenericMessageInfo.Builder()
                .id("GetExerciseOrderAndFilter.Error")
                .title("")
                .description(GET_EXERCISE_FILTER_AND_ORDER_FAILED)
                .messageType(MessageType.Error)
                .uiComponentType(UIComponentType.Toast)
        ))
    }

    companion object{
        val GET_EXERCISE_FILTER_AND_ORDER_SUCCESS = "Filter and order successfully retrieved from datastore."
        val GET_EXERCISE_FILTER_AND_ORDER_FAILED = "Error loading filter and order from datastore."
    }
}