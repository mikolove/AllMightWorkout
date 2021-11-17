package com.mikolove.allmightworkout.business.interactors.main.common

import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.interactors.main.common.GetTotalExercises.Companion.GET_TOTAL_EXERCISES_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseViewState
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

        getTotalExercises.getTotalExercises<ExerciseViewState>(
            stateEvent = GetTotalExercisesEvent()
        ).collect( object : FlowCollector<DataState<ExerciseViewState>?> {
            override suspend fun emit(value: DataState<ExerciseViewState>?) {
                Assertions.assertEquals(
                    value?.message?.response?.message,
                    GET_TOTAL_EXERCISES_SUCCESS
                )

                totalExercises = value?.data?.totalExercises ?:0
            }
        })

        val totalExerciseInCache = exerciseCacheDataSource.getTotalExercises()
        Assertions.assertTrue {  totalExerciseInCache == totalExercises }
    }
}