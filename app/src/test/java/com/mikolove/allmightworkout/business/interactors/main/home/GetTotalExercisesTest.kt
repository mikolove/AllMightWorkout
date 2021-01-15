package com.mikolove.allmightworkout.business.interactors.main.home

import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.interactors.main.home.GetTotalExercises.Companion.GET_TOTAL_EXERCISES_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.home.state.HomeStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.home.state.HomeViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/*
Test cases:
1. getTotalExercises_success_confirmCorrect()
    a) get the number of exercises in cache
    b) listen for GET_TOTAL_EXERCISES_SUCCESS from flow emission
    c) compare with the number of exercises in the fake data set
*/

@InternalCoroutinesApi
class GetTotalExercisesTest {

    //System in test
    private val getTotalExercises : GetTotalExercises

    //Dependencies
    private val dependencyContainer : DependencyContainer
    private val exerciseCacheDataSource : ExerciseCacheDataSource

    init {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        exerciseCacheDataSource = dependencyContainer.exerciseCacheDataSource
        getTotalExercises = GetTotalExercises(
            exerciseCacheDataSource
        )
    }

    @Test
    fun getTotalExercises_success_confirmCorrect() = runBlocking {

        var totalExercises = 0

        getTotalExercises.getTotalExercises(
            stateEvent = GetTotalExercisesEvent()
        ).collect( object : FlowCollector<DataState<HomeViewState>?> {
            override suspend fun emit(value: DataState<HomeViewState>?) {
                Assertions.assertEquals(
                    value?.stateMessage?.response?.message,
                    GET_TOTAL_EXERCISES_SUCCESS
                )

                totalExercises = value?.data?.numExercisesInCache ?:0
            }
        })

        val totalExerciseInCache = exerciseCacheDataSource.getTotalExercises()
        Assertions.assertTrue {  totalExerciseInCache == totalExercises }
    }
}