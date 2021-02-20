package com.mikolove.allmightworkout.business.interactors.main.workout

import com.mikolove.allmightworkout.business.data.cache.FORCE_DELETE_WORKOUT_EXCEPTION
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.model.WorkoutFactory
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.interactors.main.workout.RemoveMultipleWorkouts.Companion.DELETE_WORKOUTS_ERRORS
import com.mikolove.allmightworkout.business.interactors.main.workout.RemoveMultipleWorkouts.Companion.DELETE_WORKOUTS_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.home.state.HomeStateEvent.RemoveMultipleWorkoutsEvent
import com.mikolove.allmightworkout.framework.presentation.main.workout.state.WorkoutViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.ArrayList

/*
Test cases:
1. deleteWorkouts_success_confirmNetworkAndCacheUpdated()
    a) select a handful of random workouts for deleting
    b) delete from cache and network
    c) confirm DELETE_WORKOUTS_SUCCESS msg is emitted from flow
    d) confirm workouts are deleted from cache
    e) confirm workouts are deleted from "workouts" in network
2. deleteWorkouts_fail_confirmCorrectDeletesMade()
    - This is a complex one:
        - The use-case will attempt to delete all workouts passed as input. If there
        is an error with a particular delete, it continues with the others. But the
        resulting msg is DELETE_WORKOUTS_ERRORS. So we need to do rigorous checks here
        to make sure the correct workouts were deleted and the correct workouts were not.
    a) select a handful of random workouts for deleting
    b) change the ids of a few workouts so they will cause errors when deleting
    c) confirm DELETE_WORKOUTS_ERRORS msg is emitted from flow
    d) confirm ONLY the valid workouts are deleted from network
    e) confirm ONLY the valid workouts are deleted from cache
    f) confirm invalid workouts are not deleted from cache
3. throwException_checkGenericError_confirmNetworkAndCacheUnchanged()
    a) select a handful of random workouts for deleting
    b) force an exception to be thrown on one of them
    c) confirm DELETE_WORKOUTS_ERRORS msg is emitted from flow
    d) confirm ONLY the valid workouts are deleted from network
    e) confirm ONLY the valid workouts are deleted from cache
 */
@InternalCoroutinesApi
class RemoveMultipleWorkoutsTest {

    private var removeMultipleWorkouts : RemoveMultipleWorkouts? = null

    //Dependencies
    private lateinit var dependencyContainer : DependencyContainer
    private lateinit var workoutCacheDataSource : WorkoutCacheDataSource
    private lateinit var workoutNetworkDataSource : WorkoutNetworkDataSource
    private lateinit var workoutFactory: WorkoutFactory

    @AfterEach
    fun afterEach(){
        removeMultipleWorkouts = null
    }

    @BeforeEach
    fun beforeEach(){
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        workoutCacheDataSource = dependencyContainer.workoutCacheDataSource
        workoutNetworkDataSource = dependencyContainer.workoutNetworkDataSource
        workoutFactory = dependencyContainer.workoutFactory

        removeMultipleWorkouts = RemoveMultipleWorkouts(
            workoutCacheDataSource,
            workoutNetworkDataSource
        )
    }

    @Test
    fun deleteWorkouts_success_confirmNetworkAndCacheUpdated() = runBlocking {

        val randomWorkouts : ArrayList<Workout> = ArrayList()
        val workoutsInCache = workoutCacheDataSource.getWorkouts("","",1)

        for(workout in workoutsInCache){
            randomWorkouts.add(workout)
            if(randomWorkouts.size >4)
                break
        }

        removeMultipleWorkouts?.removeMultipleWorkouts(
            workouts = randomWorkouts,
            stateEvent = RemoveMultipleWorkoutsEvent(workouts =randomWorkouts)
        )?.collect( object : FlowCollector<DataState<WorkoutViewState>?> {
            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
                    DELETE_WORKOUTS_SUCCESS
                )
            }
        })

        //Confirm workouts were deleted from cache
        for(workout in randomWorkouts){
            val workoutInCache = workoutCacheDataSource.getWorkoutById(workout.idWorkout)
            assertTrue { workoutInCache == null }
        }

        //Confirm workouts wre deleted from network
        val doWorkoutsExistInNetwork = workoutNetworkDataSource.getWorkouts().containsAll(randomWorkouts)
        assertFalse { doWorkoutsExistInNetwork }
    }

    @Test
    fun deleteWorkouts_fail_confirmCorrectDeletesMade() = runBlocking {

        val validWorkouts : ArrayList<Workout> = ArrayList()
        val invalidWorkouts : ArrayList<Workout> = ArrayList()
        val workoutsInCache = workoutCacheDataSource.getWorkouts("","",1)
        for(index in 0..workoutsInCache.size){
            var workout : Workout
            if(index % 2 == 0){ // If divisble by 2
                workout = workoutFactory.createWorkout(
                    idWorkout = UUID.randomUUID().toString(),
                    name = workoutsInCache[index].name,
                    exercises = null,
                    created_at = null
                )
                invalidWorkouts.add(workout)
            }else{
                workout = workoutsInCache[index]
                validWorkouts.add(workout)
            }
            if( (invalidWorkouts.size + validWorkouts.size) > 4 )
                break
        }

        val workoutsToDelete = ArrayList(validWorkouts+invalidWorkouts)
        removeMultipleWorkouts?.removeMultipleWorkouts(
            workouts = workoutsToDelete,
            stateEvent = RemoveMultipleWorkoutsEvent(workouts = workoutsToDelete)
        )?.collect( object : FlowCollector<DataState<WorkoutViewState>?> {
            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
                    DELETE_WORKOUTS_ERRORS
                )
            }
        })

        //Confirm only valid workouts are deleted from network
        var networkWorkouts = workoutNetworkDataSource.getWorkouts()
        assertFalse { networkWorkouts.containsAll(validWorkouts)}

        //Confirm only valid workouts are deleted from cache
        for(workout in validWorkouts){
            val workoutInCache = workoutCacheDataSource.getWorkoutById(workout.idWorkout)
            assertTrue{ workoutInCache == null}
        }

        //Confirm invalid workouts are not deleted
        val totalWorkoutInCache = workoutCacheDataSource.getTotalWorkout()
        assertTrue { totalWorkoutInCache == (workoutsInCache.size - validWorkouts.size) }

    }

    @Test
    fun throwException_checkGenericError_confirmNetworkAndCacheUnchanged() = runBlocking {

        val validWorkouts : ArrayList<Workout> = ArrayList()
        val invalidWorkouts : ArrayList<Workout> = ArrayList()
        val workoutsInCache = workoutCacheDataSource.getWorkouts("","",1)
        for(workout in workoutsInCache){
            validWorkouts.add(workout)
            if( validWorkouts.size > 4 )
                break
        }

        val invalidWorkout = workoutFactory.createWorkout(
            idWorkout = FORCE_DELETE_WORKOUT_EXCEPTION,
            name = UUID.randomUUID().toString(),
            exercises = null,
            created_at = null
        )
        invalidWorkouts.add(invalidWorkout)

        val workoutsToDelete = ArrayList(validWorkouts+invalidWorkouts)
        removeMultipleWorkouts?.removeMultipleWorkouts(
            workouts = workoutsToDelete,
            stateEvent = RemoveMultipleWorkoutsEvent(workouts = workoutsToDelete)
        )?.collect( object : FlowCollector<DataState<WorkoutViewState>?> {
            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
                    DELETE_WORKOUTS_ERRORS
                )
            }
        })

        //Confirm only valid workouts are deleted from network
        var networkWorkouts = workoutNetworkDataSource.getWorkouts()
        assertFalse { networkWorkouts.containsAll(validWorkouts)}

        //Confirm only valid workouts are deleted from cache
        for(workout in validWorkouts){
            val workoutInCache = workoutCacheDataSource.getWorkoutById(workout.idWorkout)
            assertTrue{ workoutInCache == null}
        }

        //Confirm invalid workouts are not deleted
        val totalWorkoutInCache = workoutCacheDataSource.getTotalWorkout()
        assertTrue { totalWorkoutInCache == (workoutsInCache.size - validWorkouts.size) }

    }
}