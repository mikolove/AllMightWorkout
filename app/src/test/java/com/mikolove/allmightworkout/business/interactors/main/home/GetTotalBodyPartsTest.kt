package com.mikolove.allmightworkout.business.interactors.main.home

import com.mikolove.allmightworkout.business.data.cache.abstraction.BodyPartCacheDataSource
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.interactors.main.home.GetTotalBodyParts.Companion.GET_TOTAL_BODYPART_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.home.state.HomeStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.home.state.HomeViewState
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

        getTotalBodyParts.getTotalBodyParts(
            stateEvent = GetTotalBodyPartsEvent()
        ).collect( object : FlowCollector<DataState<HomeViewState>?>{
            override suspend fun emit(value: DataState<HomeViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
                    GET_TOTAL_BODYPART_SUCCESS
                )

                totalBodyParts = value?.data?.numBodyParts ?:0
            }
        })

        val totalBodyPartsInCache = bodyPartCacheDataSource.getTotalBodyParts()
        assertTrue{ totalBodyParts == totalBodyPartsInCache}
    }
}