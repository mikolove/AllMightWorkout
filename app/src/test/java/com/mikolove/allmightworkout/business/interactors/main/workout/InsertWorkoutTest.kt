package com.mikolove.allmightworkout.business.interactors.main.workout

import kotlinx.coroutines.InternalCoroutinesApi

/*
Test cases:
1. insertWorkout_success_confirmNetworkAndCacheUpdated()
    a) insert a new workout
    b) listen for INSERT_WORKOUT_SUCCESS emission from flow
    c) confirm cache was updated with new workout
    d) confirm network was updated with new workout
2. insertWorkout_fail_confirmNetworkAndCacheUnchanged()
    a) insert a new workout
    b) force a failure (return -1 from db operation)
    c) listen for INSERT_WORKOUT_FAILED emission from flow
    e) confirm cache was not updated
    e) confirm network was not updated
3. throwException_checkGenericError_confirmNetworkAndCacheUnchanged()
    a) insert a new workout
    b) force an exception
    c) listen for CACHE_ERROR_UNKNOWN emission from flow
    e) confirm cache was not updated
    e) confirm network was not updated
 */

@InternalCoroutinesApi
class InsertWorkoutTest {

/*
    private val insertWorkout : InsertWorkout

    //Dependencies
    private val dependencyContainer : DependencyContainer
    private val workoutCacheDataSource : WorkoutCacheDataSource
    private val workoutNetworkDataSource : WorkoutNetworkDataSource

    private val workoutFactory : WorkoutFactory

    init {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        workoutCacheDataSource = dependencyContainer.workoutCacheDataSource
        workoutNetworkDataSource = dependencyContainer.workoutNetworkDataSource
        workoutFactory = dependencyContainer.workoutFactory
        insertWorkout = InsertWorkout(
            workoutCacheDataSource,
            workoutNetworkDataSource,
            workoutFactory
        )
    }

    //Use runBlocking ok with Test could use Coroutines usual syntax also
    @Test
    fun insertWorkout_success_confirmNetworkAndCacheUpdated() = runBlocking {

        val newWorkout = workoutFactory.createWorkout(
            idWorkout = null,
            name = UUID.randomUUID().toString(),
            exercises = null,
            created_at = null
        )

        insertWorkout.execute(
            idWorkout = newWorkout.idWorkout,
            name = newWorkout.name,
            stateEvent = InsertWorkoutEvent(name = newWorkout.name)
        ).collect(object : FlowCollector<DataState<WorkoutViewState>?> {

            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assertEquals(
                    value?.message?.response?.message,
                    INSERT_WORKOUT_SUCCESS
                )
            }
        })

        //Confirm cache was updated
        val cacheWorkoutThatWasInserted = workoutCacheDataSource.getWorkoutById(newWorkout.idWorkout)
        assertTrue { cacheWorkoutThatWasInserted == newWorkout }

        //Confirm network was updated
        val networkWorkoutThatWasInserted = workoutNetworkDataSource.getWorkoutById(newWorkout.idWorkout)
        assertTrue { networkWorkoutThatWasInserted == newWorkout }
    }

     @Test
     fun insertWorkout_fail_confirmNetworkAndCacheUnchanged() = runBlocking {

         val newWorkout = workoutFactory.createWorkout(
             idWorkout = FORCE_GENERAL_FAILURE,
             name = UUID.randomUUID().toString(),
             exercises = null,
             created_at = null
         )

         insertWorkout.execute(
             idWorkout = newWorkout.idWorkout,
             name = newWorkout.name,
             stateEvent = InsertWorkoutEvent(name = newWorkout.name)
         ).collect(object : FlowCollector<DataState<WorkoutViewState>?> {

             override suspend fun emit(value: DataState<WorkoutViewState>?) {
                 assertEquals(
                     value?.message?.response?.message,
                     INSERT_WORKOUT_FAILED
                 )
             }
         })

         //Confirm cache was not updated
         val cacheWorkoutThatWasInserted = workoutCacheDataSource.getWorkoutById(newWorkout.idWorkout)
         assertTrue { cacheWorkoutThatWasInserted == null }

         //Confirm network was not updated
         val networkWorkoutThatWasInserted = workoutNetworkDataSource.getWorkoutById(newWorkout.idWorkout)
         assertTrue { networkWorkoutThatWasInserted == null }
     }

    @Test
    fun throwException_checkGenericError_confirmNetworkAndCacheUnchanged() = runBlocking {

        val newWorkout = workoutFactory.createWorkout(
            idWorkout = FORCE_NEW_WORKOUT_EXCEPTION,
            name = UUID.randomUUID().toString(),
            exercises = null,
            created_at = null
        )

        insertWorkout.execute(
            idWorkout = newWorkout.idWorkout,
            name = newWorkout.name,
            stateEvent = InsertWorkoutEvent(name = newWorkout.name)
        ).collect(object : FlowCollector<DataState<WorkoutViewState>?> {

            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assert(
                    value?.message?.response?.message
                        ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
                )
            }
        })

        //Confirm cache was not updated
        val cacheWorkoutThatWasInserted = workoutCacheDataSource.getWorkoutById(newWorkout.idWorkout)
        assertTrue { cacheWorkoutThatWasInserted == null }

        //Confirm network was not updated
        val networkWorkoutThatWasInserted = workoutNetworkDataSource.getWorkoutById(newWorkout.idWorkout)
        assertTrue { networkWorkoutThatWasInserted == null }
    }
*/

}