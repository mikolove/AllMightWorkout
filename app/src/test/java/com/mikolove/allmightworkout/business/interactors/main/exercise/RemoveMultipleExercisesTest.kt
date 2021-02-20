package com.mikolove.allmightworkout.business.interactors.main.exercise

import com.mikolove.allmightworkout.business.data.cache.FORCE_DELETE_EXERCISE_EXCEPTION
import com.mikolove.allmightworkout.business.data.cache.abstraction.BodyPartCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseFactory
import com.mikolove.allmightworkout.business.domain.model.ExerciseType
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.interactors.main.exercise.RemoveMultipleExercises.Companion.DELETE_EXERCISES_ERRORS
import com.mikolove.allmightworkout.business.interactors.main.exercise.RemoveMultipleExercises.Companion.DELETE_EXERCISES_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseStateEvent
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.ArrayList

/*
Test cases:
1. deleteExercises_success_confirmNetworkAndCacheUpdated()
    a) select a handful of random exercises for deleting
    b) delete from cache and network
    c) confirm DELETE_EXERCISES_SUCCESS msg is emitted from flow
    d) confirm exercises are deleted from cache
    e) confirm exercises are deleted from "exercises" in network
2. deleteExercises_fail_confirmCorrectDeletesMade()
    a) select a handful of random exercises for deleting
    b) change the ids of a few exercises so they will cause errors when deleting
    c) confirm DELETE_EXERCISES_ERRORS msg is emitted from flow
    d) confirm ONLY the valid exercises are deleted from network
    e) confirm ONLY the valid exercises are deleted from cache
    f) confirm invalid exercises are not deleted from cache
3. throwException_checkGenericError_confirmNetworkAndCacheUnchanged()
    a) select a handful of random exercises for deleting
    b) force an exception to be thrown on one of them
    c) confirm DELETE_EXERCISES_ERRORS msg is emitted from flow
    d) confirm ONLY the valid exercises are deleted from network
    e) confirm ONLY the valid exercises are deleted from cache
 */

@InternalCoroutinesApi
class RemoveMultipleExercisesTest {

    private var removeMultipleExercises : RemoveMultipleExercises? = null

    //Dependencies
    private lateinit var dependencyContainer : DependencyContainer
    private lateinit var exerciseCacheDataSource: ExerciseCacheDataSource
    private lateinit var exerciseNetworkDataSource: ExerciseNetworkDataSource
    private lateinit var bodyPartCacheDataSource: BodyPartCacheDataSource
    private lateinit var exerciseFactory : ExerciseFactory


    @AfterEach
    fun afterEach(){
        removeMultipleExercises = null
    }

    @BeforeEach
    fun beforeEach(){
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        exerciseCacheDataSource = dependencyContainer.exerciseCacheDataSource
        exerciseNetworkDataSource = dependencyContainer.exerciseNetworkDataSource
        bodyPartCacheDataSource = dependencyContainer.bodyPartCacheDataSource
        exerciseFactory = dependencyContainer.exerciseFactory
        removeMultipleExercises = RemoveMultipleExercises(
            exerciseCacheDataSource,
            exerciseNetworkDataSource
        )
    }

    @Test
    fun deleteExercises_success_confirmNetworkAndCacheUpdated() = runBlocking {

        val randomExercises : ArrayList<Exercise> = ArrayList()
        val exercisesInCache = exerciseCacheDataSource.getExercises("","",1)

        for(exercise in exercisesInCache){
            randomExercises.add(exercise)
            if(randomExercises.size >4)
                break
        }

        removeMultipleExercises?.removeMultipleExercises(
            exercises = randomExercises,
            stateEvent = RemoveMultipleExercisesEvent(exercises = randomExercises)
        )?.collect( object : FlowCollector<DataState<ExerciseViewState>?> {
            override suspend fun emit(value: DataState<ExerciseViewState>?) {
                Assertions.assertEquals(
                    value?.stateMessage?.response?.message,
                    DELETE_EXERCISES_SUCCESS
                )
            }
        })

        //Confirm workouts were deleted from cache
        for(exercise in randomExercises){
            val exerciseInCache = exerciseCacheDataSource.getExerciseById(exercise.idExercise)
            Assertions.assertTrue { exerciseInCache == null }
        }

        //Confirm workouts wre deleted from network
        val doExercisesExistInNetwork = exerciseNetworkDataSource.getExercises().containsAll(randomExercises)
        Assertions.assertFalse { doExercisesExistInNetwork }
    }

    @Test
    fun deleteExercises_fail_confirmCorrectDeletesMade() = runBlocking {

        val bodyPart = bodyPartCacheDataSource.getBodyPartById("idBodyPart1")
        val validExercises : ArrayList<Exercise> = ArrayList()
        val invalidExercises : ArrayList<Exercise> = ArrayList()
        val exerciseInCache = exerciseCacheDataSource.getExercises("","",1)

        for(index in 0..exerciseInCache.size){
            var exercise : Exercise
            if(index % 2 == 0){ // If divisble by 2
                exercise = exerciseFactory.createExercise(
                    idExercise = UUID.randomUUID().toString(),
                    name = exerciseInCache[index].name,
                    sets = null,
                    bodyPart = bodyPart!!,
                    exerciseType = ExerciseType.REP_EXERCISE,
                    created_at = null
                )
                invalidExercises.add(exercise)
            }else{
                exercise = exerciseInCache[index]
                validExercises.add(exercise)
            }
            if( (invalidExercises.size + validExercises.size) > 4 )
                break
        }

        val exercisesToDelete = ArrayList(validExercises+invalidExercises)
        removeMultipleExercises?.removeMultipleExercises(
            exercises = exercisesToDelete,
            stateEvent = RemoveMultipleExercisesEvent(exercises = exercisesToDelete)
        )?.collect( object : FlowCollector<DataState<ExerciseViewState>?> {
            override suspend fun emit(value: DataState<ExerciseViewState>?) {
                Assertions.assertEquals(
                    value?.stateMessage?.response?.message,
                    DELETE_EXERCISES_ERRORS
                )
            }
        })

        //Confirm only valid workouts are deleted from network
        var networkExercises = exerciseNetworkDataSource.getExercises()
        Assertions.assertFalse { networkExercises.containsAll(validExercises) }

        //Confirm only valid workouts are deleted from cache
        for(exercise in validExercises){
            val exercisesInCache = exerciseCacheDataSource.getExerciseById(exercise.idExercise)
            Assertions.assertTrue { exercisesInCache == null }
        }

        //Confirm invalid workouts are not deleted
        val totalExercisesInCache = exerciseCacheDataSource.getTotalExercises()
        Assertions.assertTrue { totalExercisesInCache == (exerciseInCache.size - validExercises.size) }

    }

    @Test
    fun throwException_checkGenericError_confirmNetworkAndCacheUnchanged() = runBlocking {

        val bodyPart = bodyPartCacheDataSource.getBodyPartById("idBodyPart1")
        val validExercises: ArrayList<Exercise> = ArrayList()
        val invalidExercises: ArrayList<Exercise> = ArrayList()
        val exerciseInCache = exerciseCacheDataSource.getExercises("", "", 1)

        for (exercise in exerciseInCache) {
            validExercises.add(exercise)
            if (validExercises.size > 4)
                break
        }

        val invalidExercise = exerciseFactory.createExercise(
            idExercise = FORCE_DELETE_EXERCISE_EXCEPTION,
            name = "name",
            sets = null,
            bodyPart = bodyPart!!,
            exerciseType = ExerciseType.REP_EXERCISE,
            created_at = null
        )
        invalidExercises.add(invalidExercise)

        val exercisesToDelete = ArrayList(validExercises + invalidExercises)
        removeMultipleExercises?.removeMultipleExercises(
            exercises = exercisesToDelete,
            stateEvent = RemoveMultipleExercisesEvent(exercises = exercisesToDelete)
        )?.collect(object : FlowCollector<DataState<ExerciseViewState>?> {
            override suspend fun emit(value: DataState<ExerciseViewState>?) {
                Assertions.assertEquals(
                    value?.stateMessage?.response?.message,
                    DELETE_EXERCISES_ERRORS
                )
            }
        })

        //Confirm only valid workouts are deleted from network
        var networkExercises = exerciseNetworkDataSource.getExercises()
        Assertions.assertFalse { networkExercises.containsAll(validExercises) }

        //Confirm only valid workouts are deleted from cache
        for (exercise in validExercises) {
            val exercisesInCache = exerciseCacheDataSource.getExerciseById(exercise.idExercise)
            Assertions.assertTrue { exercisesInCache == null }
        }

        //Confirm invalid workouts are not deleted
        val totalExercisesInCache = exerciseCacheDataSource.getTotalExercises()
        Assertions.assertTrue { totalExercisesInCache == (exerciseInCache.size - validExercises.size) }
    }
}