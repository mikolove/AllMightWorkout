package com.mikolove.allmightworkout.framework.datasource.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseFactory
import com.mikolove.allmightworkout.business.domain.model.ExerciseType
import com.mikolove.allmightworkout.business.domain.model.WorkoutFactory
import com.mikolove.allmightworkout.di.ProductionModule
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.WorkoutFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.implementation.WorkoutFirestoreServiceImpl
import com.mikolove.allmightworkout.framework.datasource.network.mappers.ExerciseNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.mappers.WorkoutNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreAuth.FIRESTORE_LOGIN
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreAuth.FIRESTORE_PASSWORD
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.test.assertEquals

@UninstallModules(ProductionModule::class)
@HiltAndroidTest
class WorkoutFirestoreServiceTest {

    //System in test
    private lateinit var workoutFirestoreService : WorkoutFirestoreService

    @Inject
    lateinit var firestore : FirebaseFirestore

    @Inject
    lateinit var firebaseAuth : FirebaseAuth

    @Inject
    lateinit var exerciseFactory : ExerciseFactory

    @Inject
    lateinit var workoutFactory : WorkoutFactory

    @Inject
    lateinit var workoutNetworkMapper : WorkoutNetworkMapper

    @Inject
    lateinit var exerciseNetworkMapper : ExerciseNetworkMapper

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init(){

        hiltRule.inject()
        signIn()

        workoutFirestoreService = WorkoutFirestoreServiceImpl(
            firebaseAuth = firebaseAuth,
            firestore = firestore,
            workoutNetworkMapper = workoutNetworkMapper,
            exerciseNetworkMapper = exerciseNetworkMapper
        )
    }

    private fun signIn() = runBlocking {
        firebaseAuth.signInWithEmailAndPassword(
            FIRESTORE_LOGIN,
            FIRESTORE_PASSWORD
        ).await()
    }

    @Test
    fun insertSingleWorkout() = runBlocking{

        var exercise = exerciseFactory.createExercise(
            idExercise = UUID.randomUUID().toString(),
            name = null,
            sets = null,
            bodyPart = null,
            exerciseType = ExerciseType.REP_EXERCISE,
            isActive = true,
            created_at = null
        )

        val listOfExercise : ArrayList<Exercise> = ArrayList()
        listOfExercise.add(exercise)
        val workout = workoutFactory.createWorkout(
            idWorkout = UUID.randomUUID().toString(),
            name = null,
            exercises = listOfExercise,
            isActive = true,
            created_at = null
        )

        workoutFirestoreService.insertWorkout(workout)

        val searchResult = workoutFirestoreService.getWorkoutById(workout.idWorkout)

        assertEquals(workout, searchResult)
    }
}