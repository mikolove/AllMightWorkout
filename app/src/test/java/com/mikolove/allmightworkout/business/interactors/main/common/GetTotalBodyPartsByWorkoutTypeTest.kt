package com.mikolove.allmightworkout.business.interactors.main.common

import com.mikolove.core.data.bodypart.abstraction.BodyPartCacheDataSource
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.interactors.common.GetTotalBodyPartsByWorkoutType.Companion.GET_TOTAL_BODYPART_BY_WORKOUTTYPE_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/*
Test cases:
1. getTotalBodyPartsByWorkoutType_success_confirmCorrect()
    a) get the number of bodyparts in cache
    b) listen for GET_TOTAL_BODYPART_SUCCESS from flow emission
    c) compare with the number of bodyparts in the fake data set
*/

@InternalCoroutinesApi
class GetTotalBodyPartsByWorkoutTypeTest {

    //System in test
    private val getTotalBodyPartsByWorkoutType : com.mikolove.core.interactors.common.GetTotalBodyPartsByWorkoutType

    //Dependencies
    private val dependencyContainer : DependencyContainer
    private val bodyPartCacheDataSource : BodyPartCacheDataSource

    init {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        bodyPartCacheDataSource = dependencyContainer.bodyPartCacheDataSource
        getTotalBodyPartsByWorkoutType =
            com.mikolove.core.interactors.common.GetTotalBodyPartsByWorkoutType(
                bodyPartCacheDataSource
            )
    }

    @Test
    fun getTotalBodyPartsByWorkoutType_success_confirmCorrect() = runBlocking {

        var totalBodyParts = 0
        val idWorkoutType = "idWorkoutType1"

        getTotalBodyPartsByWorkoutType.getTotalBodyPartsByWorkoutType<WorkoutViewState>(
            idWorkoutType = idWorkoutType,
            stateEvent = GetTotalBodyPartsByWorkoutTypeEvent(idWorkoutType)
        ).collect( object : FlowCollector<DataState<WorkoutViewState>?> {
            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                Assertions.assertEquals(
                    value?.message?.response?.message,
                    GET_TOTAL_BODYPART_BY_WORKOUTTYPE_SUCCESS
                )

                totalBodyParts = value?.data?.totalBodyPartsByWorkoutType ?:0
            }
        })

        val totalBodyPartsInCache = bodyPartCacheDataSource.getTotalBodyPartsByWorkoutType(idWorkoutType)
        Assertions.assertTrue { totalBodyParts == totalBodyPartsInCache }

    }
}