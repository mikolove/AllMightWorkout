package com.mikolove.allmightworkout.framework.datasource.cache

import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.di.ProductionModule
import com.mikolove.allmightworkout.framework.BaseTest
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.HistoryExerciseDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.HistoryExerciseSetDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.HistoryWorkoutDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.database.HistoryExerciseSetDao
import com.mikolove.allmightworkout.framework.datasource.cache.implementation.HistoryExerciseSetDaoServiceImpl
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.*
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import java.util.*
import javax.inject.Inject
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/*

1. a_insertHistoryExerciseSet_CBS
2. b_getHistoryExerciseSetById_CBS
3. c_getHistoryExerciseSetsByHistoryExercise_CBS
4. d_getTotalHistoryExerciseSet_CBS

 */
@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class HistoryExerciseSetDaoServiceTest : BaseTest(){

    //System in test
    private lateinit var historyExerciseSetDaoService : HistoryExerciseSetDaoService

    @Inject
    lateinit var historyExerciseSetDao : HistoryExerciseSetDao

    @Inject
    lateinit var historyWorkoutCacheMapper : HistoryWorkoutCacheMapper

    @Inject
    lateinit var historyWorkoutWithExercisesCacheMapper : HistoryWorkoutWithExercisesCacheMapper

    @Inject
    lateinit var historyExerciseCacheMapper : HistoryExerciseCacheMapper

    @Inject
    lateinit var historyExerciseSetCacheMapper : HistoryExerciseSetCacheMapper

    @Inject
    lateinit var historyExerciseWithSetsCacheMapper: HistoryExerciseWithSetsCacheMapper

    @Inject
    lateinit var historyWorkoutDaoService : HistoryWorkoutDaoService

    @Inject
    lateinit var historyExerciseDaoService : HistoryExerciseDaoService

    @Inject
    lateinit var historyWorkoutFactory : HistoryWorkoutFactory

    @Inject
    lateinit var historyExerciseFactory: HistoryExerciseFactory

    @Inject
    lateinit var historyExerciseSetFactory: HistoryExerciseSetFactory

    @Inject
    lateinit var dateUtil: DateUtil

    override fun injectTest() {
        hiltRule.inject()
    }
    @Before
    fun init(){
        injectTest()

        historyExerciseSetDaoService = HistoryExerciseSetDaoServiceImpl(
            historyExerciseSetDao,
            historyExerciseSetCacheMapper = historyExerciseSetCacheMapper

        )
    }

    //    1. a_insertHistoryExerciseSet_CBS
    @Test
    fun a_insertHistoryExerciseSet_CBS() = runBlocking {

        val historyExercise = insertData()

        val historyExerciseSet = historyExercise.historySets?.get(0)

        if(historyExerciseSet != null){

            val searchResult = historyExerciseSetDaoService.getHistoryExerciseSetById(historyExerciseSet.idHistoryExerciseSet)
            assertEquals(searchResult,historyExerciseSet)
        }

    }

    //    2. b_getHistoryExerciseSetById_CBS
    @Test
    fun b_getHistoryExerciseSetById_CBS() = runBlocking {

        val historyExercise = insertData()

        val historyExerciseSet = historyExercise.historySets?.get(0)

        if(historyExerciseSet != null){

            val searchResult = historyExerciseSetDaoService.getHistoryExerciseSetById(historyExerciseSet.idHistoryExerciseSet)
            assertEquals(searchResult,historyExerciseSet)
        }

    }

    //    3. c_getHistoryExerciseSetsByHistoryExercise_CBS
    @Test
    fun c_getHistoryExerciseSetsByHistoryExercise_CBS() = runBlocking {

        val historyExercise = insertData()

        val historyExerciseSet = historyExercise.historySets?.get(0)

        if(historyExerciseSet != null){

            val searchResult = historyExerciseSetDaoService.getHistoryExerciseSetsByHistoryExercise(historyExercise.idHistoryExercise)
            assertTrue { searchResult.contains(historyExerciseSet) }
        }

    }

    //    4. d_getTotalHistoryExerciseSet_CBS
    @Test
    fun d_getTotalHistoryExerciseSet_CBS() = runBlocking {

        val historyExercise = insertData()

        val totalHistoryExerciseSet = historyExerciseSetDaoService.getTotalHistoryExerciseSet(historyExercise.idHistoryExercise)

        assertEquals(totalHistoryExerciseSet,1)
    }

    private fun createHistoryWorkout() : HistoryWorkout = historyWorkoutFactory.createHistoryWorkout(
        idHistoryWorkout = UUID.randomUUID().toString(),
        name = "History workout name",
        historyExercises = null,
        started_at = dateUtil.getCurrentTimestamp(),
        ended_at = dateUtil.getCurrentTimestamp(),
        created_at = dateUtil.getCurrentTimestamp()
    )

    private fun createHistoryExercise() : HistoryExercise = historyExerciseFactory.createHistoryExercise(
        idHistoryExercise = UUID.randomUUID().toString(),
        name = "History Exercise name",
        bodyPart = "bodypart",
        workoutType = "workoutType",
        exerciseType = "exercise type",
        historySets = null,
        started_at = dateUtil.getCurrentTimestamp(),
        ended_at = dateUtil.getCurrentTimestamp(),
        created_at = dateUtil.getCurrentTimestamp()
    )

    private fun createHistoryExerciseSet() : HistoryExerciseSet = historyExerciseSetFactory.createHistoryExerciseSet(
        idHistoryExerciseSet = UUID.randomUUID().toString(),
        reps = null,
        weight = null,
        restTime = null,
        time = null,
        started_at = dateUtil.getCurrentTimestamp(),
        ended_at = dateUtil.getCurrentTimestamp(),
        created_at = dateUtil.getCurrentTimestamp()
    )

    private suspend fun insertData(): HistoryExercise {

        val historyWorkout = createHistoryWorkout()

        val historyExercise = createHistoryExercise()

        val historyExerciseSet = createHistoryExerciseSet()

        historyExercise.historySets = listOf(historyExerciseSet)

        historyWorkout.historyExercises = listOf(historyExercise)

        historyWorkoutDaoService.insertHistoryWorkout(historyWorkout)

        historyExerciseDaoService.insertHistoryExercise(historyExercise,historyWorkout.idHistoryWorkout)

        historyExerciseSetDaoService.insertHistoryExerciseSet(historyExerciseSet,historyExercise.idHistoryExercise)

        return historyExercise
    }

}