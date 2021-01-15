package com.mikolove.allmightworkout.business.interactors.main.home

import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.ExerciseFactory
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.home.state.HomeViewState
import com.mikolove.allmightworkout.framework.presentation.main.manageworkout.state.ManageWorkoutStateEvent
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Test

/*
Test cases :
1. getExercisesFromWorkoutId_success_confirmExercisesRetrieved()
    a) search exercises for specific workout id
    b) listen for GET_EXERCISES_FROM_WORKOUT_SUCCESS emitted from flow
    c) confirm exercises were retrieved
    d) confirm exercises in cache for workout id match
2. getExercisesFromWorkoutId_success_confirmNoResults()
    a) search exercises for specific workout id without exercises
    b) listen for GET_EXERCISES_FROM_WORKOUT_NO_RESULT emitted from flow
    c) confirm there is no result
    d) confirm there is no result in cache
2. getExercisesFromWorkoutId_fail_confirmNoResults()
    a) force an exception to be thrown
    b) listen for CACHE_ERROR_UNKNOWN emitted from flow
    c) confirm there is no result
    d) confirm there is no result in cache
 */

/*
TODO: Define if usefull thinking not

@InternalCoroutinesApi
class GetExercisesFromWorkoutIdTest {

    //System in test
    private val getExercisesFromWorkoutId : GetExercisesFromWorkoutId


}*/
