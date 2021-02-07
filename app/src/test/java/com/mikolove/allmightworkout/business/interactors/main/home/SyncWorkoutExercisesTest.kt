package com.mikolove.allmightworkout.business.interactors.main.home

import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseNetworkDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.di.DependencyContainer
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import java.util.*

/*

*Due to FakeData structure I just check for dateUpdate after execution
*All test
*Don't understand why I have to recreate workout generated must be tired i hope

Test cases:
In all those case check Workout/Exercise link
1. insertCachedWorkoutExercisesIntoNetwork()
    a) insert a bunch of new workout linked with exercise into the cache
    b) perform the sync
    c) check to see that those workouts were inserted into the network
2. insertNetworkWorkoutExercisesIntoCache()
    a) insert a bunch of new workout linked with exercise into the network
    b) perform the sync
    c) check to see that those workouts were inserted into the cache
3. confirmSyncCacheHasNewest()
4. confirmSyncNetworkHasNewest()
5. confirmSyncCacheAndNetworkEquals()

 */

class SyncWorkoutExercisesTest
{

    //System in test
    private val syncWorkoutExercises : SyncWorkoutExercises

    //Dependencies
    private val dependencyContainer : DependencyContainer

    private val workoutFactory : WorkoutFactory
    private val exerciseFactory : ExerciseFactory

    private val workoutCacheDataSource : WorkoutCacheDataSource
    private val workoutNetworkDataSource : WorkoutNetworkDataSource
    private val exerciseCacheDataSource : ExerciseCacheDataSource
    private val exerciseNetworkDataSource : ExerciseNetworkDataSource

    private val dateUtil : DateUtil

    init {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        workoutFactory = dependencyContainer.workoutFactory
        exerciseFactory = dependencyContainer.exerciseFactory
        dateUtil = dependencyContainer.dateUtil

        workoutCacheDataSource = dependencyContainer.workoutCacheDataSource
        workoutNetworkDataSource = dependencyContainer.workoutNetworkDataSource
        exerciseCacheDataSource = dependencyContainer.exerciseCacheDataSource
        exerciseNetworkDataSource = dependencyContainer.exerciseNetworkDataSource

        syncWorkoutExercises = SyncWorkoutExercises(
            workoutCacheDataSource,
            workoutNetworkDataSource,
            exerciseCacheDataSource,
            exerciseNetworkDataSource,
            dateUtil
        )

    }

    @Test
    fun insertCachedWorkoutExercisesIntoNetwork() = runBlocking {

        val updatedAt = dateUtil.getCurrentTimestamp()
        val workouts = createWorkouts()

        workouts.forEach { workout ->
            val test = workoutFactory.createWorkout(
                idWorkout = workout.idWorkout,
                name = workout.name,
                exercises = workout.exercises,
                isActive = workout.isActive,
                created_at = workout.createdAt
            )
            test.exerciseIdsUpdatedAt = updatedAt
            workoutCacheDataSource.insertWorkout(test)

        }

        workouts.forEach { workout ->
            val test = workoutFactory.createWorkout(
                idWorkout = workout.idWorkout,
                name = workout.name,
                exercises = workout.exercises,
                isActive = workout.isActive,
                created_at = workout.createdAt
            )
            test.exerciseIdsUpdatedAt = null
            workoutNetworkDataSource.insertWorkout(test)

        }

        syncWorkoutExercises.syncWorkoutExercises()

        workouts.forEach { workout ->

            val searchIsUpdatedC = workoutCacheDataSource.getWorkoutById(workout.idWorkout)
            val searchIsUpdatedN = workoutNetworkDataSource.getWorkoutById(workout.idWorkout)
            assertEquals(searchIsUpdatedC?.exerciseIdsUpdatedAt,searchIsUpdatedN?.exerciseIdsUpdatedAt)
        }

    }

    @Test
    fun insertNetworkWorkoutExercisesIntoCache() = runBlocking {

        val updatedAt = dateUtil.getCurrentTimestamp()
        val workouts = createWorkouts()

        workouts.forEach { workout ->
            val test = workoutFactory.createWorkout(
                idWorkout = workout.idWorkout,
                name = workout.name,
                exercises = workout.exercises,
                isActive = workout.isActive,
                created_at = workout.createdAt
            )
            test.exerciseIdsUpdatedAt = null
            workoutCacheDataSource.insertWorkout(test)

        }

        workouts.forEach { workout ->
            val test = workoutFactory.createWorkout(
                idWorkout = workout.idWorkout,
                name = workout.name,
                exercises = workout.exercises,
                isActive = workout.isActive,
                created_at = workout.createdAt
            )
            test.exerciseIdsUpdatedAt = updatedAt
            workoutNetworkDataSource.insertWorkout(test)

        }

        syncWorkoutExercises.syncWorkoutExercises()

        workouts.forEach { workout ->

            val searchIsUpdatedC = workoutCacheDataSource.getWorkoutById(workout.idWorkout)
            val searchIsUpdatedN = workoutNetworkDataSource.getWorkoutById(workout.idWorkout)
            assertEquals(searchIsUpdatedC?.exerciseIdsUpdatedAt,searchIsUpdatedN?.exerciseIdsUpdatedAt)
        }

    }

    @Test
    fun confirmSyncCacheHasNewest() = runBlocking {


        val workouts = createWorkouts()

        val updatedAtNetwork = dateUtil.getCurrentTimestamp()
        workouts.forEach { workout ->
            val test = workoutFactory.createWorkout(
                idWorkout = workout.idWorkout,
                name = workout.name,
                exercises = workout.exercises,
                isActive = workout.isActive,
                created_at = workout.createdAt
            )
            test.exerciseIdsUpdatedAt = updatedAtNetwork
            workoutNetworkDataSource.insertWorkout(test)
        }

        delay(2000)
        val updatedAtCache = dateUtil.getCurrentTimestamp()
        workouts.forEach { workout ->
            val test = workoutFactory.createWorkout(
                idWorkout = workout.idWorkout,
                name = workout.name,
                exercises = workout.exercises,
                isActive = workout.isActive,
                created_at = workout.createdAt
            )
            test.exerciseIdsUpdatedAt = updatedAtCache
            workoutCacheDataSource.insertWorkout(test)
        }


        syncWorkoutExercises.syncWorkoutExercises()

        workouts.forEach { workout ->

            val searchIsUpdatedC = workoutCacheDataSource.getWorkoutById(workout.idWorkout)
            val searchIsUpdatedN = workoutNetworkDataSource.getWorkoutById(workout.idWorkout)
            assertEquals(searchIsUpdatedC?.exerciseIdsUpdatedAt,searchIsUpdatedN?.exerciseIdsUpdatedAt)
        }

    }

    @Test
    fun confirmSyncNetworkHasNewest() = runBlocking {

        val workouts = createWorkouts()

        val updatedAtCache = dateUtil.getCurrentTimestamp()
        workouts.forEach { workout ->
            val test = workoutFactory.createWorkout(
                idWorkout = workout.idWorkout,
                name = workout.name,
                exercises = workout.exercises,
                isActive = workout.isActive,
                created_at = workout.createdAt
            )
            test.exerciseIdsUpdatedAt = updatedAtCache
            workoutCacheDataSource.insertWorkout(test)
        }

        delay(2000)

        val updatedAtNetwork = dateUtil.getCurrentTimestamp()
        workouts.forEach { workout ->
            val test = workoutFactory.createWorkout(
                idWorkout = workout.idWorkout,
                name = workout.name,
                exercises = workout.exercises,
                isActive = workout.isActive,
                created_at = workout.createdAt
            )
            test.exerciseIdsUpdatedAt = updatedAtNetwork
            workoutNetworkDataSource.insertWorkout(test)
        }

        syncWorkoutExercises.syncWorkoutExercises()

        workouts.forEach { workout ->

            val searchIsUpdatedC = workoutCacheDataSource.getWorkoutById(workout.idWorkout)
            val searchIsUpdatedN = workoutNetworkDataSource.getWorkoutById(workout.idWorkout)
            assertEquals(searchIsUpdatedC?.exerciseIdsUpdatedAt,searchIsUpdatedN?.exerciseIdsUpdatedAt)
        }
    }

    @Test
    fun confirmSyncCacheAndNetworkEquals() = runBlocking {

        val workouts = createWorkouts()

        val updatedAt = dateUtil.getCurrentTimestamp()
        workouts.forEach { workout ->
            val test = workoutFactory.createWorkout(
                idWorkout = workout.idWorkout,
                name = workout.name,
                exercises = workout.exercises,
                isActive = workout.isActive,
                created_at = workout.createdAt
            )
            test.exerciseIdsUpdatedAt = updatedAt
            workoutCacheDataSource.insertWorkout(test)
        }


        workouts.forEach { workout ->
            val test = workoutFactory.createWorkout(
                idWorkout = workout.idWorkout,
                name = workout.name,
                exercises = workout.exercises,
                isActive = workout.isActive,
                created_at = workout.createdAt
            )
            test.exerciseIdsUpdatedAt = updatedAt
            workoutNetworkDataSource.insertWorkout(test)
        }

        syncWorkoutExercises.syncWorkoutExercises()

        workouts.forEach { workout ->

            val searchIsUpdatedC = workoutCacheDataSource.getWorkoutById(workout.idWorkout)
            val searchIsUpdatedN = workoutNetworkDataSource.getWorkoutById(workout.idWorkout)
            assertEquals(searchIsUpdatedC?.exerciseIdsUpdatedAt,updatedAt)
            assertEquals(searchIsUpdatedN?.exerciseIdsUpdatedAt,updatedAt)
        }

    }

    private fun createExercise() : Exercise = exerciseFactory.createExercise(
        idExercise = UUID.randomUUID().toString(),
        name = "mon exo",
        sets = null,
        bodyPart = null,
        exerciseType = ExerciseType.REP_EXERCISE,
        isActive = true,
        created_at = null
    )
    private fun createWorkout(exercises: List<Exercise>) : Workout = workoutFactory.createWorkout(
        idWorkout = UUID.randomUUID().toString(),
        name = "MES TEST",
        exercises = exercises,
        isActive = true,
        created_at = null
    )

    private fun createExercises() : List<Exercise> {
        val list : ArrayList<Exercise> = ArrayList()
        (1..1).forEach {
            list.add(createExercise())
        }
        return list
    }
    private fun createWorkouts() : List<Workout> {
        val exercises = createExercises()
        val list : ArrayList<Workout> = ArrayList()
        (1..3).forEach {
            list.add(createWorkout(exercises))
        }
        return list
    }
}