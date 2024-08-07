package com.mikolove.allmightworkout.business.interactors.main.workout

import com.mikolove.core.domain.workout.WorkoutCacheDataSource
import com.mikolove.core.domain.workout.WorkoutFactory
import com.mikolove.core.domain.state.DataState
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutViewState
import com.mikolove.core.interactors.workout.GetTotalWorkouts
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/*
Test cases:
1. getTotalWorkouts_success_confirmCorrect()
    a) get the number of workouts in cache
    b) listen for GET_TOTAL_WORKOUT_SUCCESS from flow emission
    c) compare with the number of workouts in the fake data set
*/
@InternalCoroutinesApi
class GetTotalWorkoutsTest {

    //System in test
    private val getTotalWorkouts : com.mikolove.core.interactors.workout.GetTotalWorkouts

    //Dependencies
    private val dependencyContainer : DependencyContainer
    private val workoutCacheDataSource : WorkoutCacheDataSource
    private val workoutFactory : WorkoutFactory

    init {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        workoutCacheDataSource = dependencyContainer.workoutCacheDataSource
        workoutFactory = dependencyContainer.workoutFactory
        getTotalWorkouts = com.mikolove.core.interactors.workout.GetTotalWorkouts(
            workoutCacheDataSource
        )
    }

    @Test
    fun getTotalWorkouts_success_confirmCorrect() = runBlocking {

        var totalWorkouts = 0

        getTotalWorkouts.execute(
            stateEvent = GetTotalWorkoutsEvent()
        ).collect( object : FlowCollector<DataState<WorkoutViewState>?> {
            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assertEquals(
                    value?.message?.response?.message,
                    com.mikolove.core.interactors.workout.GetTotalWorkouts.GET_TOTAL_WORKOUT_SUCCESS
                )

                totalWorkouts = value?.data?.totalWorkouts ?:0
            }
        })

        val totalWorkoutInCache = workoutCacheDataSource.getTotalWorkout()
        assertTrue {  totalWorkoutInCache == totalWorkouts }
    }
}