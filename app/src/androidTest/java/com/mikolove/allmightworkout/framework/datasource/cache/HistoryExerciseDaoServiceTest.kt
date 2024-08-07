package com.mikolove.allmightworkout.framework.datasource.cache

import com.mikolove.allmightworkout.framework.BaseTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters

/*

1. a_insertHistoryExercise_CBS
2. b_getHistoryExercisesByHistoryWorkout_CBS
3. c_getHistoryExerciseById_CBS
4. d_getTotalHistoryExercise_CBS

 */
@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class HistoryExerciseDaoServiceTest : BaseTest() {
    /*
    //System in test
    private lateinit var historyExerciseDaoService : HistoryExerciseDaoService

    @Inject
    lateinit var historyExerciseDao : HistoryExerciseDao

    @Inject
    lateinit var historyWorkoutCacheMapper : HistoryWorkoutCacheMapper

    @Inject
    lateinit var historyWorkoutWithExercisesCacheMapper : HistoryWorkoutWithExercisesCacheMapper

    @Inject
    lateinit var historyExerciseCacheMapper : HistoryExerciseCacheMapper

    @Inject
    lateinit var historyExerciseWithSetsCacheMapper: HistoryExerciseWithSetsCacheMapper

    @Inject
    lateinit var historyWorkoutDaoService : HistoryWorkoutDaoService

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

    override fun injectTest() {
        hiltRule.inject()
    }

    @Before
    fun init(){
        injectTest()

        historyExerciseDaoService = HistoryExerciseDaoServiceImpl(
            historyExerciseDao,
            historyExerciseCacheMapper = historyExerciseCacheMapper,
            historyExerciseWithSetsCacheMapper = historyExerciseWithSetsCacheMapper
        )
    }

    //    1. a_insertHistoryExercise_CBS
    @Test
    fun a_insertHistoryExercise_CBS() = runBlocking {

        val historyWorkout = insertData()

        val historyExercise = historyWorkout.historyExercises?.get(0)

        if(historyExercise != null){
            val searchResult = historyExerciseDaoService.getHistoryExerciseById(historyExercise?.idHistoryExercise)
            assertEquals(searchResult,historyExercise)
        }

    }

    //    2. b_getHistoryExercisesByHistoryWorkout_CBS
    @Test
    fun b_getHistoryExercisesByHistoryWorkout_CBS() = runBlocking {

        val historyWorkout = insertData()

        val historyExercise = historyWorkout.historyExercises?.get(0)

        if(historyExercise != null){

            val searchResult = historyExerciseDaoService.getHistoryExercisesByHistoryWorkout(historyWorkout.idHistoryWorkout)
            assertTrue {  searchResult?.contains(historyExercise) == true }

        }

    }

    //    3. c_getHistoryExerciseById_CBS
    @Test
    fun c_getHistoryExerciseById_CBS() = runBlocking {

        val historyWorkout = insertData()

        val historyExercise = historyWorkout.historyExercises?.get(0)

        if(historyExercise != null){

            val searchResult = historyExerciseDaoService.getHistoryExerciseById(historyExercise.idHistoryExercise)
            assertEquals(searchResult,historyExercise)

        }
    }

    //    4. d_getTotalHistoryExercise_CBS
    @Test
    fun d_getTotalHistoryExercise_CBS() = runBlocking {

        val historyWorkout = insertData()
        val historyWorkoutTwo = insertData()

        val totalHistoryExercise = historyExerciseDaoService.getTotalHistoryExercise()

        assertEquals(totalHistoryExercise,2)
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
    }*/
    override fun injectTest() {
        TODO("Not yet implemented")
    }
}