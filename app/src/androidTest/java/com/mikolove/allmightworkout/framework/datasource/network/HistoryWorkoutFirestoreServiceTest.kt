package com.mikolove.allmightworkout.framework.datasource.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.business.domain.model.HistoryExerciseFactory
import com.mikolove.allmightworkout.business.domain.model.HistoryExerciseSetFactory
import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import com.mikolove.allmightworkout.business.domain.model.HistoryWorkoutFactory
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.BaseTest
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.HistoryWorkoutFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.implementation.HistoryWorkoutFirestoreServiceImpl
import com.mikolove.allmightworkout.framework.datasource.network.mappers.HistoryExerciseNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.mappers.HistoryExerciseSetNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.mappers.HistoryWorkoutNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreAuth
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
import kotlin.test.assertNotEquals


/*
LEGEND:
CBS = "Confirm by searching"
Test cases:

1. a_insertHistoryWorkoutAndGet_CBS
2. b_getLastHistoryWorkout_CBS
3. c_getHistoryWorkout_CBS

 */

@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class HistoryWorkoutFirestoreServiceTest : BaseTest(){

    //System in test
    private lateinit var historyWorkoutFirestoreService : HistoryWorkoutFirestoreService

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
    fun init(){

        injectTest()
        //signIn()

        historyWorkoutFirestoreService = HistoryWorkoutFirestoreServiceImpl(
            firebaseAuth = firebaseAuth ,
            firestore =firestore,
            historyWorkoutNetworkMapper = historyWorkoutNetworkMapper,
            historyExerciseNetworkMapper = historyExerciseNetworkMapper,
            historyExerciseSetNetworkMapper = historyExerciseSetNetworkMapper,
            dateUtil = dateUtil
        )
    }

    @Test
    fun a_insertHistoryWorkoutAndGet_CBS() = runBlocking {

        val historyWorkout = createHistoryWorkout()

        historyWorkoutFirestoreService.insertHistoryWorkout(historyWorkout)

        //CBS
        val searchResult = historyWorkoutFirestoreService.getHistoryWorkoutById(historyWorkout.idHistoryWorkout)

        assertEquals(historyWorkout,searchResult)
    }

    @Test
    fun b_getLastHistoryWorkout_CBS() = runBlocking {

        val historyWorkout =historyWorkoutFactory.createHistoryWorkout(
            idHistoryWorkout = UUID.randomUUID().toString(),
            name = "new history workout last 4 month",
            historyExercises = null,
            started_at = dateUtil.getCurrentTimestamp(),
            ended_at = dateUtil.getCurrentTimestamp(),
            created_at = dateUtil.convertDateToStringDate(
                dateUtil.getCurrentDateLessMonth(4)
            )
        )

        val totalBeforeInsert = historyWorkoutFirestoreService.getLastHistoryWorkout()?.size

        historyWorkoutFirestoreService.insertHistoryWorkout(historyWorkout)

        val totalAfterInsert = historyWorkoutFirestoreService.getLastHistoryWorkout()?.size

        val totalInBase = historyWorkoutFirestoreService.getHistoryWorkout()?.size

        assertEquals(totalBeforeInsert,totalAfterInsert)
        assertNotEquals(totalAfterInsert,totalInBase)
    }

    @Test
    fun c_getHistoryWorkout_CBS() = runBlocking{

        val historyWorkout = createHistoryWorkout()

        val totalBeforeInsert = historyWorkoutFirestoreService.getHistoryWorkout()?.size

        historyWorkoutFirestoreService.insertHistoryWorkout(historyWorkout)

        val totalAfterInsert = historyWorkoutFirestoreService.getHistoryWorkout()?.size

        assertEquals(totalBeforeInsert?.plus(1),totalAfterInsert)

    }

    private fun createHistoryWorkout() : HistoryWorkout = historyWorkoutFactory.createHistoryWorkout(
        idHistoryWorkout = UUID.randomUUID().toString(),
        name = "new history workout",
        historyExercises = null,
        started_at = dateUtil.getCurrentTimestamp(),
        ended_at = dateUtil.getCurrentTimestamp(),
        created_at = dateUtil.getCurrentTimestamp()
    )
}