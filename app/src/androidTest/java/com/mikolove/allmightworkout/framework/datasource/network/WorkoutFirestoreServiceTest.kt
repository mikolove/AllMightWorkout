package com.mikolove.allmightworkout.framework.datasource.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.di.ProductionModule
import com.mikolove.allmightworkout.framework.BaseTest
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.WorkoutFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.implementation.WorkoutFirestoreServiceImpl
import com.mikolove.allmightworkout.framework.datasource.network.mappers.ExerciseNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.mappers.WorkoutNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreAuth.FIRESTORE_LOGIN
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreAuth.FIRESTORE_PASSWORD
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import java.util.*
import javax.inject.Inject
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/*
LEGEND:
CBS = "Confirm by searching"
Test cases:
1. a_insertWorkout_CBS
2. b_updateWorkout_CBS
3. c_removeWorkout_CBS
4. d_getWorkout
5. e_insertDeleteWorkout_CBS
6. f_insertDeleteWorkouts_CBS
7. g_updateExerciseIdsUpdatedAt_CBS
 */

@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class WorkoutFirestoreServiceTest : BaseTest() {

    //System in test
    private lateinit var workoutFirestoreService : WorkoutFirestoreService

    //Init hilt and dependencies
    @Inject
    lateinit var firestore : FirebaseFirestore

    @Inject
    lateinit var firebaseAuth : FirebaseAuth


    //Dependencies
    @Inject
    lateinit var exerciseFactory : ExerciseFactory

    @Inject
    lateinit var workoutFactory : WorkoutFactory

    @Inject
    lateinit var workoutNetworkMapper : WorkoutNetworkMapper

    @Inject
    lateinit var exerciseNetworkMapper : ExerciseNetworkMapper

    @Inject
    lateinit var dateUtil : DateUtil

    override  fun injectTest() {
        hiltRule.inject()
    }

    private fun signIn() = runBlocking {
        firebaseAuth.signInWithEmailAndPassword(
            FIRESTORE_LOGIN,
            FIRESTORE_PASSWORD
        ).await()
    }

    @Before
    fun setup(){

        injectTest()
        //signIn()

        workoutFirestoreService = WorkoutFirestoreServiceImpl(
            firebaseAuth = firebaseAuth,
            firestore = firestore,
            workoutNetworkMapper = workoutNetworkMapper,
            exerciseNetworkMapper = exerciseNetworkMapper,
            dateUtil = dateUtil
        )
    }


    @Test
    fun a_insertWorkout_CBS() = runBlocking{

        val workout = workoutFactory.createWorkout(
            idWorkout = UUID.randomUUID().toString(),
            name = null,
            exercises = null,
            isActive = true,
            created_at = null
        )

        workoutFirestoreService.insertWorkout(workout)

        val searchResult = workoutFirestoreService.getWorkoutById(workout.idWorkout)

        assertEquals(workout, searchResult)

        //Just to fill with one
        val anotherWorkout = workoutFactory.createWorkout(
            idWorkout = UUID.randomUUID().toString(),
            name = null,
            exercises = null,
            isActive = true,
            created_at = null
        )
        workoutFirestoreService.insertWorkout(anotherWorkout)

    }

    @Test
    fun b_updateWorkout_CBS() = runBlocking{

        val workouts = workoutFirestoreService.getWorkouts()

        if(workouts.isNotEmpty()){

            val workout = workouts.get(0)
            val updatedWorkout = workoutFactory.createWorkout(
                idWorkout = workout.idWorkout,
                name = "New workout updated",
                exercises = null,
                isActive = false,
                created_at = workout.createdAt
            )

            workoutFirestoreService.updateWorkout(updatedWorkout)

            val searchResult = workoutFirestoreService.getWorkoutById(workout.idWorkout)

            //updatedAt always news should work every run
            assertEquals(updatedWorkout,searchResult)
        }

    }

    @Test
    fun c_removeWorkout_CBS() = runBlocking {

        val workouts = workoutFirestoreService.getWorkouts()

        if (workouts.isNotEmpty()) {

            val workout = workouts.get(0)

            workoutFirestoreService.removeWorkout(workout.idWorkout)

            val searchResult = workoutFirestoreService.getWorkoutById(workout.idWorkout)

            assertEquals(searchResult,null)
        }

    }

    @Test
    fun d_getWorkout() = runBlocking {

        val workouts = workoutFirestoreService.getWorkouts()
        val totalNumber = workoutFirestoreService.getWorkoutTotalNumber()

        assertEquals(workouts.size,totalNumber)
    }

    @Test
    fun e_insertDeleteWorkout_CBS() = runBlocking {

        val workout = workoutFactory.createWorkout(
            idWorkout = UUID.randomUUID().toString(),
            name = null,
            exercises = null,
            isActive = true,
            created_at = null
        )

        workoutFirestoreService.insertDeleteWorkout(workout)

        val searchResult = workoutFirestoreService.getDeletedWorkouts()

        assertTrue { searchResult.contains(workout) }
    }

    @Test
    fun f_insertDeleteWorkouts_CBS() = runBlocking {

        val workout = workoutFactory.createWorkout(
            idWorkout = UUID.randomUUID().toString(),
            name = null,
            exercises = null,
            isActive = true,
            created_at = null
        )
        val workout2 = workoutFactory.createWorkout(
            idWorkout = UUID.randomUUID().toString(),
            name = null,
            exercises = null,
            isActive = true,
            created_at = null
        )
        val listOfWorkouts = listOf(workout,workout2)

        workoutFirestoreService.insertDeleteWorkouts(listOfWorkouts)

        val searchResult = workoutFirestoreService.getDeletedWorkouts()

        assertTrue { searchResult.containsAll(listOfWorkouts) }
    }

    @Test
    fun g_updateExerciseIdsUpdatedAt_CBS() = runBlocking {

        val workouts = workoutFirestoreService.getWorkouts()

        if (workouts.isNotEmpty()) {

            val workout = workouts.get(0)

            //Not null assert
            val updatedAt = dateUtil.getCurrentTimestamp()

            workoutFirestoreService.updateExerciseIdsUpdatedAt(workout.idWorkout, updatedAt)

            val dateUpdated = workoutFirestoreService.getExerciseIdsUpdate(workout.idWorkout)

            assertEquals(dateUpdated,updatedAt)

            val searResult = workoutFirestoreService.getWorkoutById(workout.idWorkout)

            assertEquals(searResult?.exerciseIdsUpdatedAt,updatedAt)

            //Null assert
            val updatedAtNull = null

            workoutFirestoreService.updateExerciseIdsUpdatedAt(workout.idWorkout, updatedAtNull)

            val dateUpdatedNull = workoutFirestoreService.getExerciseIdsUpdate(workout.idWorkout)

            printLogD("date",dateUpdatedNull.toString())
            assertEquals(dateUpdatedNull,null)

            val searResultNull = workoutFirestoreService.getWorkoutById(workout.idWorkout)
            assertEquals(searResultNull?.exerciseIdsUpdatedAt,null)
        }

    }

}