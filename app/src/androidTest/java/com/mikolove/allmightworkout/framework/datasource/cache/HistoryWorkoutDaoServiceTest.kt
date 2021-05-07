package com.mikolove.allmightworkout.framework.datasource.cache

import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.di.ProductionModule
import com.mikolove.allmightworkout.framework.BaseTest
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.HistoryExerciseDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.HistoryExerciseSetDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.HistoryWorkoutDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.database.HistoryWorkoutDao
import com.mikolove.allmightworkout.framework.datasource.cache.implementation.HistoryWorkoutDaoServiceImpl
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.HistoryWorkoutCacheMapper
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.HistoryWorkoutWithExercisesCacheMapper
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import java.util.*
import javax.inject.Inject
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/*

1. a_insertHistoryWorkout_CBS
2. b_getHistoryWorkoutById_CBS
3. c_getTotalHistoryWorkout_CBS
4. d_getHistoryWorkouts_CBS
5. e_getHistoryWorkoutOrderByDateDESC_CBS
6. f_getHistoryWorkoutOrderByDateASC_CBS
7. g_getHistoryWorkoutOrderByNameDESC_CBS
8. h_getHistoryWorkoutOrderByNameASC_CBS
9. i_getHistoryWorkoutByName_CBS

 */
@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class HistoryWorkoutDaoServiceTest : BaseTest(){

    //System in test
    private lateinit var historyWorkoutDaoService : HistoryWorkoutDaoService

    @Inject
    lateinit var historyWorkoutDao : HistoryWorkoutDao

    @Inject
    lateinit var historyWorkoutCacheMapper : HistoryWorkoutCacheMapper

    @Inject
    lateinit var historyWorkoutWithExercisesCacheMapper : HistoryWorkoutWithExercisesCacheMapper

    @Inject
    lateinit var historyExerciseDaoService : HistoryExerciseDaoService

    @Inject
    lateinit var historyExerciseSetDaoService : HistoryExerciseSetDaoService

    @Inject
    lateinit var historyWorkoutFactory : HistoryWorkoutFactory

    @Inject
    lateinit var historyExerciseFactory: HistoryExerciseFactory

    @Inject
    lateinit var historyExerciseSetFactory: HistoryExerciseSetFactory

    @Inject
    lateinit var dateUtil: DateUtil

    @Before
    fun init(){
        injectTest()

        historyWorkoutDaoService = HistoryWorkoutDaoServiceImpl(
            historyWorkoutDao = historyWorkoutDao,
            historyWorkoutCacheMapper = historyWorkoutCacheMapper,
            historyWorkoutWithExercisesCacheMapper = historyWorkoutWithExercisesCacheMapper
        )
    }

    override fun injectTest() {
        hiltRule.inject()
    }

    //    1. a_insertHistoryWorkout_CBS
    @Test
    fun a_insertHistoryWorkout_CBS() = runBlocking {

        val historyWorkout = insertData()

        val searchResult = historyWorkoutDaoService.getHistoryWorkoutById(historyWorkout.idHistoryWorkout)

        assertEquals(historyWorkout,searchResult)

    }

    //    2. b_getHistoryWorkoutById_CBS
    @Test
    fun b_getHistoryWorkoutById_CBS() = runBlocking {

        val historyWorkout = createHistoryWorkout()

        historyWorkoutDaoService.insertHistoryWorkout(historyWorkout)

        val searchResult = historyWorkoutDaoService.getHistoryWorkoutById(historyWorkout.idHistoryWorkout)

        assertEquals(historyWorkout,searchResult)

    }

    //    3. c_getTotalHistoryWorkout_CBS
    @Test
    fun c_getTotalHistoryWorkout_CBS() = runBlocking {

        val historyWorkout = insertData()

        val totalHistory = historyWorkoutDaoService.getTotalHistoryWorkout()

        assertEquals(totalHistory,1)

    }

    //    4. d_getHistoryWorkouts_CBS
    @Test
    fun d_getHistoryWorkouts_CBS() = runBlocking {

        val historyWorkout = insertData()
        val historyWorkoutTwo = insertData()

        val searchResult = historyWorkoutDaoService.getHistoryWorkouts()
        assertTrue {  searchResult.containsAll(listOf(historyWorkout,historyWorkoutTwo)) }
    }

    //    5. e_getHistoryWorkoutOrderByDateDESC_CBS
    @Test
    fun e_getHistoryWorkoutOrderByDateDESC_CBS() = runBlocking {

        val historyWorkout = insertData()
        delay(1000)
        val historyWorkoutTwo = insertData()

        val listOfHistoryWorkout = listOf(historyWorkout,historyWorkoutTwo)
        val sortedHistoryWorkouts = listOfHistoryWorkout.sortedByDescending { it.createdAt }

        val searchResult = historyWorkoutDaoService.getHistoryWorkoutOrderByDateDESC("",1)

        assertTrue { sortedHistoryWorkouts.zip(searchResult).all { (x,y) -> x == y } }

    }

    //    6. f_getHistoryWorkoutOrderByDateASC_CBS
    @Test
    fun f_getHistoryWorkoutOrderByDateASC_CBS() = runBlocking {

        val historyWorkout = insertData()
        delay(1000)
        val historyWorkoutTwo = insertData()

        val listOfHistoryWorkout = listOf(historyWorkout,historyWorkoutTwo)
        val sortedHistoryWorkouts = listOfHistoryWorkout.sortedBy { it.createdAt }

        val searchResult = historyWorkoutDaoService.getHistoryWorkoutOrderByDateASC("",1)

        assertTrue { sortedHistoryWorkouts.zip(searchResult).all { (x,y) -> x == y } }

    }

    //    7. g_getHistoryWorkoutOrderByNameDESC_CBS
    @Test
    fun g_getHistoryWorkoutOrderByNameDESC_CBS() = runBlocking {

        val historyWorkout = insertData()
        val historyWorkoutTwo = insertData()

        val listOfHistoryWorkout = listOf(historyWorkout,historyWorkoutTwo)
        val sortedHistoryWorkouts = listOfHistoryWorkout.sortedByDescending { it.name }

        val searchResult = historyWorkoutDaoService.getHistoryWorkoutOrderByNameDESC("",1)

        assertTrue { sortedHistoryWorkouts.zip(searchResult).all { (x,y) -> x == y } }


    }

    //    8. h_getHistoryWorkoutOrderByNameASC_CBS
    @Test
    fun h_getHistoryWorkoutOrderByNameASC_CBS() = runBlocking {

        val historyWorkout = insertData()
        val historyWorkoutTwo = insertData()

        val listOfHistoryWorkout = listOf(historyWorkout,historyWorkoutTwo)
        val sortedHistoryWorkouts = listOfHistoryWorkout.sortedBy { it.name }

        val searchResult = historyWorkoutDaoService.getHistoryWorkoutOrderByDateASC("",1)

        assertTrue { sortedHistoryWorkouts.zip(searchResult).all { (x,y) -> x == y } }

    }

    //    9. i_getHistoryWorkoutByName_CBS
    @Test
    fun i_getHistoryWorkoutByName_CBS() = runBlocking {

        val historyWorkout = insertData()
        val historyWorkoutTwo = insertData()

        val searchResult = historyWorkoutDaoService.getHistoryWorkoutOrderByDateDESC(historyWorkout.name,1)

        assertTrue { searchResult.contains(historyWorkout) }

    }

    //    9. i_getHistoryWorkoutByName_CBS
    @Test
    fun j_deleteHistoryWorkout_CBS() = runBlocking {

        val historyWorkout = insertData()

        val searchResult = historyWorkoutDaoService.getHistoryWorkoutOrderByDateDESC(historyWorkout.name,1)

        assertTrue { searchResult.contains(historyWorkout) }

        val deleted = historyWorkoutDaoService.deleteHistoryWorkout(historyWorkout.idHistoryWorkout)

        val searchDelete = historyWorkoutDaoService.getHistoryWorkoutOrderByDateDESC(historyWorkout.name,1)

        assertTrue { searchDelete.isEmpty() }

        assertTrue {  deleted == 1 }
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

    private suspend fun insertData(): HistoryWorkout {

        val historyWorkout = createHistoryWorkout()

        val historyExercise = createHistoryExercise()

        val historyExerciseSet = createHistoryExerciseSet()

        historyExercise.historySets = listOf(historyExerciseSet)

        historyWorkout.historyExercises = listOf(historyExercise)

        historyWorkoutDaoService.insertHistoryWorkout(historyWorkout)

        historyExerciseDaoService.insertHistoryExercise(historyExercise,historyWorkout.idHistoryWorkout)

        historyExerciseSetDaoService.insertHistoryExerciseSet(historyExerciseSet,historyExercise.idHistoryExercise)

        return historyWorkout
    }

}