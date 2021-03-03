package com.mikolove.allmightworkout.framework.datasource.cache

import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.BaseTest
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.*
import com.mikolove.allmightworkout.framework.datasource.cache.database.ExerciseSetDao
import com.mikolove.allmightworkout.framework.datasource.cache.implementation.ExerciseSetDaoServiceImpl
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.ExerciseSetCacheMapper
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.ExerciseWithSetsCacheMapper
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/*

1. a_insertExerciseSet_CBS
2. b_updateExerciseSet_CBS
3. c_removeExerciseSets_CBS
4. d_removeExerciseSetById_CBS
5. e_getExerciseSetById_CBS
6. f_getExerciseSetByIdExercise_CBS
7. g_getTotalExercisesSetByExercise_CBS

 */
@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ExerciseSetDaoServiceTest : BaseTest(){

    //Sytem in test
    private lateinit var exerciseSetDaoService: ExerciseSetDaoService

    @Inject
    lateinit var exerciseSetDao : ExerciseSetDao

    @Inject
    lateinit var exerciseSetCacheMapper: ExerciseSetCacheMapper

    @Inject
    lateinit var exerciseWithSetCacheMapper : ExerciseWithSetsCacheMapper

    @Inject
    lateinit var dateUtil: DateUtil

    @Inject
    lateinit var workoutFactory: WorkoutFactory

    //Generate fake dataSet should isolate this
    @Inject
    lateinit var exerciseFactory : ExerciseFactory

    @Inject
    lateinit var exerciseSetFactory : ExerciseSetFactory

    @Inject
    lateinit var workoutTypeFactory: WorkoutTypeFactory

    @Inject
    lateinit var bodyPartFactory : BodyPartFactory

    @Inject
    lateinit var workoutDaoService : WorkoutDaoService

    @Inject
    lateinit var exerciseDaoService : ExerciseDaoService

    @Inject
    lateinit var workoutTypeDaoService : WorkoutTypeDaoService

    @Inject
    lateinit var workoutExerciseDaoService : WorkoutExerciseDaoService

    @Inject
    lateinit var bodyPartDaoService: BodyPartDaoService

    @Before
    fun init(){
        injectTest()

        exerciseSetDaoService = ExerciseSetDaoServiceImpl(
            exerciseSetDao = exerciseSetDao,
            exerciseSetCacheMapper = exerciseSetCacheMapper,
            dateUtil = dateUtil
        )

    }
    override fun injectTest() {
        hiltRule.inject()
    }

    //    1. a_insertExerciseSet_CBS
    @Test
    fun a_insertExerciseSet_CBS() = runBlocking{

        val exercise = createExerciseOnly()
        exerciseDaoService.insertExercise(exercise)

        val set = createSetExercise()
        exercise.sets = listOf(set)

        val isInserted = exerciseSetDaoService.insertExerciseSet(set,exercise.idExercise)

        assertEquals(isInserted,1)

        val searchResult = exerciseSetDaoService.getExerciseSetById(set.idExerciseSet,exercise.idExercise)

        assertEquals(set,searchResult)
    }

    //    2. b_updateExerciseSet_CBS
    @Test
    fun b_updateExerciseSet_CBS() = runBlocking {

        val exercise = createExerciseOnly()
        exerciseDaoService.insertExercise(exercise)

        val set = createSetExercise()
        exercise.sets = listOf(set)

        exerciseSetDaoService.insertExerciseSet(set,exercise.idExercise)

        val dbSet = exerciseSetDaoService.getExerciseSetById(set.idExerciseSet,exercise.idExercise)

        val updatedSet = exerciseSetFactory.createExerciseSet(
            idExerciseSet = dbSet?.idExerciseSet,
            reps = 10,
            weight = 10,
            time =10,
            restTime = 10,
            created_at = dbSet?.createdAt
        )

        val isUpdated = exerciseSetDaoService.updateExerciseSet(
            updatedSet.idExerciseSet,
            updatedSet.reps,
            updatedSet.weight,
            updatedSet.time,
            updatedSet.restTime,
            updatedSet.updatedAt,
            exercise.idExercise
        )

        assertEquals(isUpdated,1)

        val dbSetUpdated = exerciseSetDaoService.getExerciseSetById(updatedSet.idExerciseSet,exercise.idExercise)

        assertEquals(dbSetUpdated,updatedSet)
    }

    //    3. c_removeExerciseSets_CBS
    @Test
    fun c_removeExerciseSets_CBS() = runBlocking {

        val exercise = createExerciseOnly()
        exerciseDaoService.insertExercise(exercise)

        val sets = createSets()
        exercise.sets = sets

        sets.forEach {
            exerciseSetDaoService.insertExerciseSet(it,exercise.idExercise)
        }

        exerciseSetDaoService.removeExerciseSets(sets)

        sets.forEach {
            val set = exerciseSetDaoService.getExerciseSetById(it.idExerciseSet,exercise.idExercise)
            assertEquals(set,null)
        }


    }

    //    4. d_removeExerciseSetById_CBS
    @Test
    fun d_removeExerciseSetById_CBS() = runBlocking {

        val exercise = createExerciseOnly()
        exerciseDaoService.insertExercise(exercise)

        val set = createSetExercise()
        exercise.sets = listOf(set)

        exerciseSetDaoService.insertExerciseSet(set,exercise.idExercise)

        exerciseSetDaoService.removeExerciseSetById(set.idExerciseSet,exercise.idExercise)

        val setRemoved = exerciseSetDaoService.getExerciseSetById(set.idExerciseSet,exercise.idExercise)

        assertEquals(setRemoved,null)

    }

    //    5. e_getExerciseSetById_CBS
    @Test
    fun e_getExerciseSetById_CBS() = runBlocking {

        val exercise = createExerciseOnly()
        exerciseDaoService.insertExercise(exercise)

        val set = createSetExercise()
        exercise.sets = listOf(set)

        exerciseSetDaoService.insertExerciseSet(set,exercise.idExercise)

        val searchResult = exerciseSetDaoService.getExerciseSetById(set.idExerciseSet,exercise.idExercise)

        assertEquals(searchResult,set)

    }

    //    6. f_getExerciseSetByIdExercise_CBS
    @Test
    fun f_getExerciseSetByIdExercise_CBS() = runBlocking {

        val exercise = createExerciseOnly()
        exerciseDaoService.insertExercise(exercise)

        val sets = createSets()
        exercise.sets = sets

        sets.forEach {
            exerciseSetDaoService.insertExerciseSet(it,exercise.idExercise)
        }

        val searchResult = exerciseSetDaoService.getExerciseSetByIdExercise(exercise.idExercise)
        assertTrue { searchResult?.containsAll(sets) == true }

        val exerciseTwo = createExerciseOnly()
        exerciseDaoService.insertExercise(exerciseTwo)

        val searchResultEmpty = exerciseSetDaoService.getExerciseSetByIdExercise(exerciseTwo.idExercise)
        assertTrue { searchResultEmpty.isEmpty() }

    }

    //    7. g_getTotalExercisesSetByExercise_CBS
    @Test
    fun g_getTotalExercisesSetByExercise_CBS() = runBlocking {

        val exercise = createExerciseOnly()
        exerciseDaoService.insertExercise(exercise)

        val set = createSetExercise()
        exercise.sets = listOf(set)

        exerciseSetDaoService.insertExerciseSet(set,exercise.idExercise)

        val totalSets = exerciseSetDaoService.getTotalExercisesSetByExercise(exercise.idExercise)

        assertEquals(totalSets,1)
    }

    private fun createSets() : List<ExerciseSet>{
        val listOfSet : ArrayList<ExerciseSet> = ArrayList()
        (1..4).forEach {
            listOfSet.add(createSetExercise())
        }
        return listOfSet
    }

    private fun createSetExercise() : ExerciseSet {
        val exerciseSet = exerciseSetFactory.createExerciseSet(
            idExerciseSet = UUID.randomUUID().toString(),
            reps = null,
            weight = null,
            time = null,
            restTime = null,
            created_at = null
        )
        return exerciseSet
    }

    private fun createExerciseOnly() = exerciseFactory.createExercise(
        idExercise = UUID.randomUUID().toString(),
        name = null,
        bodyPart = null,
        sets = null,
        exerciseType = ExerciseType.REP_EXERCISE,
        isActive = true,
        created_at = null)

}