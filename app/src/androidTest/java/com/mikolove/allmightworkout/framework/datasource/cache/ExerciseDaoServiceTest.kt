package com.mikolove.allmightworkout.framework.datasource.cache

import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.di.ProductionModule
import com.mikolove.allmightworkout.framework.BaseTest
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.*
import com.mikolove.allmightworkout.framework.datasource.cache.database.ExerciseDao
import com.mikolove.allmightworkout.framework.datasource.cache.implementation.ExerciseDaoServiceImpl
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.ExerciseCacheMapper
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.ExerciseWithSetsCacheMapper
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.delay
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

1. a_insertTestData_CBS
2. b_insertExercise_CBS
3. c_updateExercise_CBS
4. d_removeExerciseById_CBS
5. e_removeCascadeExercise_CBS
6. f_removeExercises_CBS
7. g_getExerciseByWorkout_CBS
9. i_getTotalExercises_CBS
10. j_getExercisesOrderByDateDESC
11. k_getExercisesOrderByDateASC
12. l_getExercises
13. m_searchExercisesByTitle
14. n_getExercisesOrderByTitleASC
15. o_getExercisesOrderByTitleDESC

 */
@UninstallModules(ProductionModule::class)
@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ExerciseDaoServiceTest : BaseTest(){

    //Sytem in test
    private lateinit var exerciseDaoService: ExerciseDaoService

    @Inject
    lateinit var exerciseDao : ExerciseDao

    @Inject
    lateinit var exerciseCacheMapper: ExerciseCacheMapper

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
    lateinit var exerciseSetDaoService : ExerciseSetDaoService

    @Inject
    lateinit var workoutTypeDaoService : WorkoutTypeDaoService

    @Inject
    lateinit var workoutExerciseDaoService : WorkoutExerciseDaoService

    @Inject
    lateinit var bodyPartDaoService: BodyPartDaoService

    @Before
    fun init(){
        injectTest()

        exerciseDaoService = ExerciseDaoServiceImpl(
            exerciseDao = exerciseDao,
            exerciseCacheMapper = exerciseCacheMapper,
            exerciseWithSetCacheMapper = exerciseWithSetCacheMapper,
            dateUtil = dateUtil
        )

    }
    override fun injectTest() {
        hiltRule.inject()
    }


//    1. a_insertTestData_CBS
    @Test
    fun a_insertTestData_CBS() = runBlocking {

        val exercise = insertTestData()

        val searchResult = exerciseDaoService.getExerciseById(exercise.idExercise)

        assertEquals(exercise,searchResult)
    }


//    2. b_insertExercise_CBS
    @Test
    fun b_insertExercise_CBS() = runBlocking {

        val exercise = createExerciseOnly()

        val isInserted = exerciseDaoService.insertExercise(exercise)

        val searchResult = exerciseDaoService.getExerciseById(exercise.idExercise)

        assertEquals(isInserted,1)
        assertEquals(searchResult,exercise)
    }

//    3. c_updateExercise_CBS

    @Test
    fun c_updateExercise_CBS() = runBlocking {

        val exercise = createExerciseOnly()

        exerciseDaoService.insertExercise(exercise)

        val searchResult = exerciseDaoService.getExerciseById(exercise.idExercise)

        assertEquals(searchResult,exercise)

        val exerciseUpdated = exerciseFactory.createExercise(
            idExercise = searchResult?.idExercise,
            name = "New name updated",
            sets = searchResult?.sets,
            bodyPart = searchResult?.bodyPart,
            exerciseType = ExerciseType.TIME_EXERCISE,
            isActive = false,
            created_at = searchResult?.createdAt
        )

        val isUpdated = exerciseDaoService.updateExercise(
            exerciseUpdated.idExercise,
            exerciseUpdated.name,
            exerciseUpdated.bodyPart,
            exerciseUpdated.isActive,
            exerciseUpdated.exerciseType.name,
            exercise.updatedAt
        )

        val searchUpdated = exerciseDaoService.getExerciseById(exerciseUpdated.idExercise)

        assertEquals(isUpdated,1)
        assertEquals(searchUpdated,exerciseUpdated)

    }

//    4. d_removeExerciseById_CBS
    @Test
    fun d_removeExerciseById_CBS() = runBlocking {

    val exercise = createExerciseOnly()

    exerciseDaoService.insertExercise(exercise)

    val searchResult = exerciseDaoService.getExerciseById(exercise.idExercise)

    assertEquals(searchResult,exercise)

    val isDeleted = exerciseDao.removeExerciseById(exercise.idExercise)

    assertEquals(isDeleted,1)

    val searchRemovedResult = exerciseDaoService.getExerciseById(exercise.idExercise)

    assertEquals(searchRemovedResult,null)

    }

//    5. e_removeCascadeExercise_CBS
    @Test
    fun e_removeCascadeExercise_CBS() = runBlocking {

        val exercise = insertTestData()

        val dbExercise = exerciseDaoService.getExerciseById(exercise.idExercise)

        assertEquals(dbExercise,exercise)

        val isDeleted = exerciseDao.removeExerciseById(exercise.idExercise)

        assertEquals(isDeleted,1)

        val searchRemovedResult = exerciseDaoService.getExerciseById(exercise.idExercise)

        assertEquals(searchRemovedResult,null)
    }

//    6. f_removeExercises_CBS
    @Test
    fun f_removeExercises_CBS() = runBlocking {

        val exerciseOne = createExerciseOnly()
        val exerciseTwo = createExerciseOnly()
        val listOfExercises = listOf(exerciseOne,exerciseTwo)

        listOfExercises.forEach {
            exerciseDaoService.insertExercise(it)
        }

        exerciseDaoService.removeExercises(listOfExercises)

        listOfExercises.forEach {
            val exercise = exerciseDaoService.getExerciseById(it.idExercise)
            assertEquals(exercise,null)
        }
    }

    //    7. g_getExerciseByWorkout_CBS
    @Test
    fun g_getExerciseByWorkout_CBS() = runBlocking {

        val workout = createWorkout()
        workoutDaoService.insertWorkout(workout)

        val exercise = createExerciseOnly()
        exerciseDaoService.insertExercise(exercise)

        workoutExerciseDaoService.addExerciseToWorkout(workout.idWorkout,exercise.idExercise)

        val searchResult = exerciseDaoService.getExercisesByWorkout(workout.idWorkout)

        assertTrue { searchResult?.contains(exercise) == true }

        val exercisesTwo = insertTestData()
        workoutExerciseDaoService.addExerciseToWorkout(workout.idWorkout,exercisesTwo.idExercise)

        val searchComplexResult = exerciseDaoService.getExercisesByWorkout(workout.idWorkout)

        assertTrue { searchComplexResult?.contains(exercise) == true }
        assertTrue { searchComplexResult?.contains(exercisesTwo) == true }


    }

    //    9. i_getTotalExercises_CBS
    @Test
    fun i_getTotalExercises_CBS() = runBlocking {

        val exercise = createExerciseOnly()

        exerciseDaoService.insertExercise(exercise)

        val totalExercises = exerciseDao.getTotalExercises()

        assertEquals(totalExercises,1)
    }

    //    10. j_getExercisesOrderByDateDESC
    @Test
    fun j_getExercisesOrderByDateDESC() = runBlocking {

        val listOfExercises : ArrayList<Exercise> = ArrayList()
        (1..5).forEach {
            val exercise = createExerciseOnly()
            delay(1000)
            listOfExercises.add(exercise)
            exerciseDaoService.insertExercise(exercise)
        }

        val searchExercises = exerciseDaoService.getExercisesOrderByDateDESC("",1)

        var previous = searchExercises.get(0).createdAt
        for(index in 1..searchExercises.size - 1){
            val current = searchExercises.get(index).createdAt
            assertTrue { current <= previous }
            previous = current
        }
    }

    //    11. k_getExercisesOrderByDateASC
    @Test
    fun k_getExercisesOrderByDateASC() = runBlocking {

        val listOfExercises : ArrayList<Exercise> = ArrayList()
        (1..5).forEach {
            val exercise = createExerciseOnly()
            delay(1000)
            listOfExercises.add(exercise)
            exerciseDaoService.insertExercise(exercise)
        }

        val searchExercises = exerciseDaoService.getExercisesOrderByDateASC("",1)

        var previous = searchExercises.get(0).createdAt
        for(index in 1..searchExercises.size - 1){
            val current = searchExercises.get(index).createdAt
            assertTrue { current >= previous }
            previous = current
        }
    }

    //    12. l_getExercises
    @Test
    fun l_getExercises() = runBlocking {

        val listOfExercises : ArrayList<Exercise> = ArrayList()
        (1..5).forEach {
            val exercise = createExerciseOnly()
            listOfExercises.add(exercise)
            exerciseDaoService.insertExercise(exercise)
        }

        val searchResult = exerciseDaoService.getExercises()

        assertTrue { listOfExercises.containsAll(searchResult) }
    }

    //    13. m_searchExercisesByTitle
    @Test
    fun m_searchExercisesByTitle() = runBlocking {

        val listOfExercises : ArrayList<Exercise> = ArrayList()
        (1..5).forEach {
            val exercise = createExerciseOnly()
            listOfExercises.add(exercise)
            exerciseDaoService.insertExercise(exercise)
        }
        (1..5).forEach {
            val exercise = exerciseFactory.createExercise(
                idExercise = UUID.randomUUID().toString(),
                name = "searched name",
                bodyPart = null,
                sets = null,
                exerciseType = ExerciseType.REP_EXERCISE,
                isActive = true,
                created_at = null)
            listOfExercises.add(exercise)
            exerciseDaoService.insertExercise(exercise)
        }

        val searchExercises = exerciseDaoService.getExercisesOrderByNameDESC("searched name", 1)

        val filteredExercises = listOfExercises.filter { it.name == "searched name" }
        assertTrue { filteredExercises.containsAll(searchExercises) }
    }

    //    14. n_getExercisesOrderByTitleASC
    @Test
    fun n_getExercisesOrderByTitleASC() = runBlocking {

        val listOfExercises : ArrayList<Exercise> = ArrayList()
        (1..5).forEach {
            val exercise = exerciseFactory.createExercise(
                idExercise = UUID.randomUUID().toString(),
                name = it.toString()+"searched name",
                bodyPart = null,
                sets = null,
                exerciseType = ExerciseType.REP_EXERCISE,
                isActive = true,
                created_at = null)
            listOfExercises.add(exercise)
            exerciseDaoService.insertExercise(exercise)
        }

        val sortedListOfExercises = listOfExercises.sortedBy { it.name }

        val searchExercises = exerciseDaoService.getExercisesOrderByNameASC("",1)

        assertTrue(sortedListOfExercises.zip(searchExercises).all { (x, y) -> x == y })

    }

    //    15. o_getExercisesOrderByTitleDESC
    @Test
    fun o_getExercisesOrderByTitleDESC() = runBlocking {

        val listOfExercises : ArrayList<Exercise> = ArrayList()
        (1..5).forEach {
            val exercise = exerciseFactory.createExercise(
                idExercise = UUID.randomUUID().toString(),
                name = it.toString()+"searched name",
                bodyPart = null,
                sets = null,
                exerciseType = ExerciseType.REP_EXERCISE,
                isActive = true,
                created_at = null)
            listOfExercises.add(exercise)
            exerciseDaoService.insertExercise(exercise)
        }

        val sortedListOfExercises = listOfExercises.sortedByDescending { it.name }

        val searchExercises = exerciseDaoService.getExercisesOrderByNameDESC("",1)

        assertTrue(sortedListOfExercises.zip(searchExercises).all { (x, y) -> x == y })

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

    private fun createExercise() : Exercise {

        val bodyPart = bodyPartFactory.createBodyPart(
            idBodyPart = "abs_lower",
            name = "lower abs"
        )
        val exercise = exerciseFactory.createExercise(
            idExercise = UUID.randomUUID().toString(),
            name = null,
            bodyPart = bodyPart,
            sets = null,
            exerciseType = ExerciseType.REP_EXERCISE,
            isActive = true,
            created_at = null)

        return exercise
    }

    private suspend fun insertTestData() : Exercise{

        //Create workoutType/bodyPart insert it
        val workoutType = workoutTypeFactory.createWorkoutType(
            idWorkoutType = "abs",
            name = "abs",
            bodyParts = null
        )

        val bodyPart = bodyPartFactory.createBodyPart(
            idBodyPart = "abs_upper",
            name = "upper abs"
        )
        workoutType.bodyParts = listOf(bodyPart)

        workoutTypeDaoService.insertWorkoutType(workoutType)
        bodyPartDaoService.insertBodyPart(bodyPart,workoutType.idWorkoutType)

        //Create workout

        val exercise = createExercise()
        val sets = createSets()

        exercise.bodyPart = bodyPart
        exercise.sets = sets

        exerciseDaoService.insertExercise(exercise)
        sets?.forEach {
            exerciseSetDaoService.insertExerciseSet(it,exercise.idExercise)
        }

        val dateUpdatedAt = dateUtil.getCurrentTimestamp()


        return exercise
    }

    private fun createWorkout() : Workout{
        val workout = workoutFactory.createWorkout(
            idWorkout = UUID.randomUUID().toString(),
            name = null,
            exercises = null,
            isActive = true,
            created_at = null
        )
        return workout
    }
}