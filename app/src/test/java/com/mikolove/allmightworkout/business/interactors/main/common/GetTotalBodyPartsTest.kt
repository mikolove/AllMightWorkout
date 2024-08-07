package com.mikolove.allmightworkout.business.interactors.main.common

import com.mikolove.allmightworkout.business.data.cache.abstraction.BodyPartCacheDataSource
import com.mikolove.core.domain.state.DataState
import com.mikolove.allmightworkout.business.interactors.main.common.GetTotalBodyParts.Companion.GET_TOTAL_BODYPART_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/*
Test cases:
1. getTotalBodyParts_success_confirmCorrect()
    a) get the number of bodyparts in cache
    b) listen for GET_TOTAL_BODYPART_SUCCESS from flow emission
    c) compare with the number of bodyparts in the fake data set
*/

@InternalCoroutinesApi
class GetTotalBodyPartsTest {

    //System in test
    private val getTotalBodyParts : GetTotalBodyParts

    //Dependencies
    private val dependencyContainer : DependencyContainer
    private val bodyPartCacheDataSource : BodyPartCacheDataSource

    init {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        bodyPartCacheDataSource = dependencyContainer.bodyPartCacheDataSource
        getTotalBodyParts = GetTotalBodyParts(
            bodyPartCacheDataSource
        )
    }

    @Test
    fun getTotalBodyParts_success_confirmCorrect() = runBlocking {

        var totalBodyParts = 0

        getTotalBodyParts.getTotalBodyParts<WorkoutViewState>(
            stateEvent = GetTotalBodyPartsEvent()
        ).collect( object : FlowCollector<DataState<WorkoutViewState>?>{
            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assertEquals(
                    value?.message?.response?.message,
                    GET_TOTAL_BODYPART_SUCCESS
                )

                totalBodyParts = value?.data?.totalBodyParts ?:0
            }
        })

        val totalBodyPartsInCache = bodyPartCacheDataSource.getTotalBodyParts()
        assertTrue{ totalBodyParts == totalBodyPartsInCache}
    }
}