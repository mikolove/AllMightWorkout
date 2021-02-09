package com.mikolove.allmightworkout.framework.datasource.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.di.ProductionModule
import com.mikolove.allmightworkout.framework.BaseTest
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.HistoryExerciseFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.HistoryExerciseSetFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.HistoryWorkoutFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.implementation.HistoryExerciseFirestoreServiceImpl
import com.mikolove.allmightworkout.framework.datasource.network.implementation.HistoryExerciseSetFirestoreServiceImpl
import com.mikolove.allmightworkout.framework.datasource.network.implementation.HistoryWorkoutFirestoreServiceImpl
import com.mikolove.allmightworkout.framework.datasource.network.mappers.HistoryExerciseNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.mappers.HistoryExerciseSetNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.mappers.HistoryWorkoutNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreAuth
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

1. a_insertHistoryExerciseSetAndGet_CBS

 */

@UninstallModules(ProductionModule::class)
@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class HistoryExerciseSetFirestoreServiceTest  : BaseTest(){

    //System in test
    private lateinit var historyExceriseSetFirestoreService : HistoryExerciseSetFirestoreService

    //Init hilt and dependencies
    private lateinit var historyExceriseFirestoreService : HistoryExerciseFirestoreService

    private lateinit var historyWorkoutFirestoreService: HistoryWorkoutFirestoreService

    @Inject
    lateinit var firestore : FirebaseFirestore

    @Inject
    lateinit var firebaseAuth : FirebaseAuth

    @Inject
    lateinit var historyWorkoutFactory: HistoryWorkoutFactory

    @Inject
    lateinit var historyExerciseFactory: HistoryExerciseFactory

    @Inject
    lateinit var historyExerciseSetFactory: HistoryExerciseSetFactory

    @Inject
    lateinit var historyWorkoutNetworkMapper: HistoryWorkoutNetworkMapper

    @Inject
    lateinit var historyExerciseNetworkMapper: HistoryExerciseNetworkMapper

    @Inject
    lateinit var historyExerciseSetNetworkMapper: HistoryExerciseSetNetworkMapper

    @Inject
    lateinit var dateUtil: DateUtil


    override fun injectTest() {
        hiltRule.inject()
    }

    private fun signIn() = runBlocking {
        firebaseAuth.signInWithEmailAndPassword(
            FirestoreAuth.FIRESTORE_LOGIN,
            FirestoreAuth.FIRESTORE_PASSWORD
        ).await()
    }

    @Before
    fun init() {

        injectTest()
        //signIn()

        historyExceriseSetFirestoreService = HistoryExerciseSetFirestoreServiceImpl(
            firebaseAuth,
            firestore,
            historyExerciseSetNetworkMapper
        )

        historyWorkoutFirestoreService = HistoryWorkoutFirestoreServiceImpl(
            firebaseAuth,
            firestore,
            historyWorkoutNetworkMapper = historyWorkoutNetworkMapper,
            historyExerciseNetworkMapper = historyExerciseNetworkMapper,
            historyExerciseSetNetworkMapper = historyExerciseSetNetworkMapper,
            dateUtil = dateUtil

        )
        historyExceriseFirestoreService = HistoryExerciseFirestoreServiceImpl(
            firebaseAuth,
            firestore,
            historyExerciseNetworkMapper,
            historyExerciseSetNetworkMapper
        )
    }

    @Test
    fun a_insertHistoryExerciseSetAndGet_CBS() = runBlocking {

        val historyWorkout = createHistoryWorkout()
        val historyExercise = createHistoryExercise()
        val historyExerciseSet = createHistoryExerciseSet()

        historyExercise.historySets = listOf(historyExerciseSet)
        historyWorkout.historyExercises = listOf(historyExercise)

        historyWorkoutFirestoreService.insertHistoryWorkout(historyWorkout)
        historyExceriseFirestoreService.insertHistoryExercise(historyExercise, historyWorkout.idHistoryWorkout)

        historyExceriseSetFirestoreService.insertHistoryExerciseSet(historyExerciseSet,historyExercise.idHistoryExercise,historyWorkout.idHistoryWorkout)

        val searchResult = historyExceriseSetFirestoreService.getHistoryExerciseSetsByHistoryExerciseId(historyExercise.idHistoryExercise,historyWorkout.idHistoryWorkout)

        assertTrue { searchResult.contains(historyExerciseSet)}
    }

    private fun createHistoryWorkout() : HistoryWorkout = historyWorkoutFactory.createHistoryWorkout(
        idHistoryWorkout = UUID.randomUUID().toString(),
        name = "new history workout",
        historyExercises = null,
        started_at = dateUtil.getCurrentTimestamp(),
        ended_at = dateUtil.getCurrentTimestamp(),
        created_at = dateUtil.getCurrentTimestamp()
    )

    private fun createHistoryExercise() : HistoryExercise = historyExerciseFactory.createHistoryExercise(
        idHistoryExercise = UUID.randomUUID().toString(),
        name = "new history exercise",
        bodyPart = "abs lower",
        workoutType = "abs",
        exerciseType = "rep_ex",
        historySets = null,
        started_at = dateUtil.getCurrentTimestamp(),
        ended_at = dateUtil.getCurrentTimestamp(),
        created_at = dateUtil.getCurrentTimestamp()
    )

    private fun createHistoryExerciseSet() : HistoryExerciseSet = historyExerciseSetFactory.createHistoryExerciseSet(
        idHistoryExerciseSet = UUID.randomUUID().toString(),
        reps = 10,
        weight = 10,
        time = 10,
        restTime = 10,
        started_at = dateUtil.getCurrentTimestamp(),
        ended_at = dateUtil.getCurrentTimestamp(),
        created_at = dateUtil.getCurrentTimestamp()
    )
}