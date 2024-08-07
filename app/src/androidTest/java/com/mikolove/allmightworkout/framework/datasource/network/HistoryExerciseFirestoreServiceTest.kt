package com.mikolove.allmightworkout.framework.datasource.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.BaseTest
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.HistoryExerciseFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.HistoryWorkoutFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.implementation.HistoryExerciseFirestoreServiceImpl
import com.mikolove.allmightworkout.framework.datasource.network.implementation.HistoryWorkoutFirestoreServiceImpl
import com.mikolove.allmightworkout.framework.datasource.network.mappers.HistoryExerciseNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.mappers.HistoryExerciseSetNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.mappers.HistoryWorkoutNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreAuth
import com.mikolove.core.domain.analytics.HistoryExercise
import com.mikolove.core.domain.analytics.HistoryExerciseFactory
import com.mikolove.core.domain.analytics.HistoryExerciseSetFactory
import com.mikolove.core.domain.analytics.HistoryWorkout
import com.mikolove.core.domain.analytics.HistoryWorkoutFactory
import dagger.hilt.android.testing.HiltAndroidTest
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

1. a_insertHistoryExerciseAndGet_CBS
2. b_getHistoryExerciseByHistoryWorkoutId_CBS

 */

@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class HistoryExerciseFirestoreServiceTest : BaseTest(){

    //System in test
    private lateinit var historyExceriseFirestoreService : HistoryExerciseFirestoreService

    private lateinit var historyWorkoutFirestoreService: HistoryWorkoutFirestoreService

    //Init hilt and dependencies
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
    fun a_insertHistoryExerciseAndGet_CBS() = runBlocking {

        val historyWorkout = createHistoryWorkout()
        val historyExercise = createHistoryExercise()
        historyWorkout.historyExercises = listOf(historyExercise)

        historyWorkoutFirestoreService.insertHistoryWorkout(historyWorkout)

        historyExceriseFirestoreService.insertHistoryExercise(historyExercise,historyWorkout.idHistoryWorkout)

        val searchResult = historyExceriseFirestoreService.getHistoryExerciseById(historyExercise.idHistoryExercise,historyWorkout.idHistoryWorkout)

        assertEquals(searchResult,historyExercise)

    }

    @Test
    fun b_getHistoryExerciseByHistoryWorkoutId_CBS() = runBlocking {

        val historyWorkout = createHistoryWorkout()
        val historyExercise = createHistoryExercise()
        historyWorkout.historyExercises = listOf(historyExercise)

        historyWorkoutFirestoreService.insertHistoryWorkout(historyWorkout)

        historyExceriseFirestoreService.insertHistoryExercise(historyExercise,historyWorkout.idHistoryWorkout)

        val searchResult = historyExceriseFirestoreService.getHistoryExerciseByHistoryWorkoutId(historyWorkout.idHistoryWorkout)

        assertTrue { historyWorkout.historyExercises?.containsAll(searchResult) == true }
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
}