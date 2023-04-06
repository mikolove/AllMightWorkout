package com.mikolove.allmightworkout.framework.datasource.cache

import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.di.ProductionModule
import com.mikolove.allmightworkout.framework.BaseTest
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.*
import com.mikolove.allmightworkout.framework.datasource.cache.database.WorkoutDao
import com.mikolove.allmightworkout.framework.datasource.cache.implementation.WorkoutDaoServiceImpl
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.WorkoutCacheMapper
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.WorkoutWithExercisesCacheMapper
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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
2. b_insertWorkout_CBS
3. c_updateWorkout_CBS
4. d_removeWorkout_CBS
5. e_removeCascadeWorkout_CBS
6. f_removeWorkouts_CBS
7. g_getExerciseIdsUpdate_CBS
8. h_updateExerciseIdsUpdatedAt_CBS
9. i_getTotalWorkout_CBS
10. j_getWorkoutsOrderByDateDESC
11. k_getWorkoutsOrderByDateASC
12. l_getWorkouts
13. m_searchWorkoutsByTitle
14. n_getWorkoutsOrderByTitleASC
15. o_getWorkoutsOrderByTitleDESC

 */
@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class WorkoutDaoServiceTest : BaseTest() {

    /* //Sytem in test
    private lateinit var workoutDaoService : WorkoutDaoService

    @Inject
    lateinit var workoutDao : WorkoutDao

    @Inject
    lateinit var workoutCacheMapper : WorkoutCacheMapper

    @Inject
    lateinit var workoutWithExerciseCacheMapper: WorkoutWithExercisesCacheMapper

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
    lateinit var exerciseDaoService : ExerciseDaoService

    @Inject
    lateinit var exerciseSetDaoService : ExerciseSetDaoService

    @Inject
    lateinit var workoutTypeDaoService : WorkoutTypeDaoService

    @Inject
    lateinit var bodyPartDaoService: BodyPartDaoService

    @Inject
    lateinit var workoutExerciseDaoService: WorkoutExerciseDaoService


    @Before
    fun init(){
        injectTest()

        workoutDaoService = WorkoutDaoServiceImpl(
            workoutDao = workoutDao,
            workoutCacheMapper = workoutCacheMapper,
            workoutWithExercisesCacheMapper = workoutWithExerciseCacheMapper,
            dateUtil = dateUtil
        )
    }
    override fun injectTest() {
        hiltRule.inject()
    }

    @Test
    fun a_insertDataTest() = runBlocking {

        val workout = insertTestData()

        val searchResult = workoutDaoService.getWorkoutById(workout.idWorkout)

        assertEquals(workout,searchResult)
    }

    @Test
    fun b_insertWorkout_CBS() = runBlocking {

        val workout = createWorkout()

        val isInserted = workoutDaoService.insertWorkout(workout)

        val searchResult = workoutDaoService.getWorkoutById(workout.idWorkout)

        assertEquals(isInserted,1)
        assertEquals(workout,searchResult)

    }

    @Test
    fun c_updateWorkout_CBS() = runBlocking {

        val workout = createWorkout()

        workoutDaoService.insertWorkout(workout)

        val dbWorkout = workoutDaoService.getWorkoutById(workout.idWorkout)

        delay(1000)
        val workoutUpdated = workoutFactory.createWorkout(
            idWorkout = dbWorkout?.idWorkout,
            name = "new name updated",
            exercises = dbWorkout?.exercises,
            isActive = false,
            created_at = dbWorkout?.createdAt
        )

        val isUpdated = workoutDaoService.updateWorkout(workoutUpdated.idWorkout,workoutUpdated.name,workoutUpdated.updatedAt,workoutUpdated.isActive)

        val dbWorkoutUpdated = workoutDaoService.getWorkoutById(workoutUpdated.idWorkout)

        assertEquals(isUpdated,1)
        assertEquals(dbWorkoutUpdated,workoutUpdated)
    }

    @Test
    fun d_removeWorkout_CBS() = runBlocking {

        val workout = createWorkout()

        workoutDaoService.insertWorkout(workout)

        val dbWorkout = workoutDaoService.getWorkoutById(workout.idWorkout)

        assertEquals(dbWorkout,workout)

        val isDeleted = workoutDaoService.removeWorkout(workout.idWorkout)

        assertEquals(isDeleted,1)

        val dbRemoveWorkout = workoutDaoService.getWorkoutById(workout.idWorkout)

        assertEquals(dbRemoveWorkout,null)

    }

    @Test
    fun e_removeCascadeWorkout_CBS() = runBlocking {

        val workout = insertTestData()

        val dbWorkout = workoutDaoService.getWorkoutById(workout.idWorkout)

        assertEquals(dbWorkout,workout)

        val isDeleted = workoutDaoService.removeWorkout(workout.idWorkout)

        assertEquals(isDeleted,1)

        val dbRemoveWorkout = workoutDaoService.getWorkoutById(workout.idWorkout)

        assertEquals(dbRemoveWorkout,null)
    }

    @Test
    fun f_removeWorkouts_CBS() = runBlocking {

        val workoutOne = createWorkout()
        val workoutTwo = createWorkout()
        val listOfWorkouts = listOf(workoutOne,workoutTwo)

        listOfWorkouts.forEach {
            workoutDaoService.insertWorkout(it)
        }

        workoutDaoService.removeWorkouts(listOfWorkouts)

        listOfWorkouts.forEach {
            val workout = workoutDaoService.getWorkoutById(it.idWorkout)
            assertEquals(workout,null)
        }
    }


    @Test
    fun  g_getExerciseIdsUpdate_CBS() = runBlocking {

        val workout = insertTestData()

        val searResult = workoutDaoService.getExerciseIdsUpdate(workout.idWorkout)

        assertEquals(workout.exerciseIdsUpdatedAt,searResult)
    }

    @Test
    fun h_updateExerciseIdsUpdatedAt_CBS() = runBlocking{

        val workout = insertTestData()

        val searchResult = workoutDaoService.getExerciseIdsUpdate(workout.idWorkout)

        assertEquals(workout.exerciseIdsUpdatedAt,searchResult)

        workout.exerciseIdsUpdatedAt = dateUtil.getCurrentTimestamp()

        workoutDaoService.updateExerciseIdsUpdatedAt(workout.idWorkout,workout.exerciseIdsUpdatedAt)

        val searchUpdatedResult = workoutDaoService.getExerciseIdsUpdate(workout.idWorkout)

        assertEquals(workout.exerciseIdsUpdatedAt,searchUpdatedResult)


    }

    @Test
    fun i_getTotalWorkout_CBS() = runBlocking {

        val workout = createWorkout()

        workoutDaoService.insertWorkout(workout)

        val totalWorkouts = workoutDaoService.getTotalWorkout()

        assertEquals(totalWorkouts,1)
    }



    @Test
    fun j_getWorkoutsOrderByDateDESC() = runBlocking {

        val listOfWorkout : ArrayList<Workout> = ArrayList()
        (1..5).forEach {
            val workout = createWorkout()
            delay(1000)
            listOfWorkout.add(workout)
            workoutDaoService.insertWorkout(workout)
        }

        val searchWorkouts = workoutDaoService.getWorkoutsOrderByDateDESC("",1)

        var previous = searchWorkouts.get(0).createdAt
        for(index in 1..searchWorkouts.size - 1){
            val current = searchWorkouts.get(index).createdAt
            assertTrue { current <= previous }
            previous = current
        }

    }

    @Test
    fun k_getWorkoutsOrderByDateASC() = runBlocking {

        val listOfWorkout : ArrayList<Workout> = ArrayList()
        (1..5).forEach {
            val workout = createWorkout()
            delay(1000)
            listOfWorkout.add(workout)
            workoutDaoService.insertWorkout(workout)
        }

        val searchWorkouts = workoutDaoService.getWorkoutsOrderByDateASC("",1)

        var previous = searchWorkouts.get(0).createdAt
        for(index in 1..searchWorkouts.size - 1){
            val current = searchWorkouts.get(index).createdAt
            assertTrue { current >= previous }
            previous = current
        }

    }

    @Test
    fun l_getWorkouts() = runBlocking {

        val listOfWorkout : ArrayList<Workout> = ArrayList()
        (1..5).forEach {
            val workout = createWorkout()
            listOfWorkout.add(workout)
            workoutDaoService.insertWorkout(workout)
        }

        val searchResult = workoutDaoService.getWorkouts()

        assertTrue{ listOfWorkout.containsAll(searchResult)}

    }

    @Test
    fun m_searchWorkoutsByTitle() = runBlocking {

        val listOfWorkout : ArrayList<Workout> = ArrayList()
        (1..5).forEach {
            val workout = createWorkout()
            listOfWorkout.add(workout)
            workoutDaoService.insertWorkout(workout)
        }
        (1..5).forEach {
            val workout = workoutFactory.createWorkout(
                idWorkout = UUID.randomUUID().toString(),
                name = "searched name",
                exercises = null,
                isActive = true,
                created_at = null
            )
            listOfWorkout.add(workout)
            workoutDaoService.insertWorkout(workout)
        }

        val searchWorkouts = workoutDaoService.getWorkoutsOrderByDateDESC("searched name",1)

        val filteredWorkouts = listOfWorkout.filter { it.name == "searched name" }
        assertTrue{ filteredWorkouts.containsAll(searchWorkouts) }

    }

    @Test
    fun n_getWorkoutsOrderByTitleASC() = runBlocking {

        val listOfWorkout : ArrayList<Workout> = ArrayList()
        (1..5).forEach {
            val workout = workoutFactory.createWorkout(
                idWorkout = UUID.randomUUID().toString(),
                name = it.toString()+"searched name",
                exercises = null,
                isActive = true,
                created_at = null
            )
            listOfWorkout.add(workout)
            workoutDaoService.insertWorkout(workout)
        }

        val sortedListOfWorkouts = listOfWorkout.sortedBy { it.name }

        val searchWorkouts = workoutDaoService.getWorkoutsOrderByNameASC("",1)

        assertTrue(sortedListOfWorkouts.zip(searchWorkouts).all { (x, y) -> x == y })
    }

    @Test
    fun o_getWorkoutsOrderByTitleDESC() = runBlocking {

        val listOfWorkout : ArrayList<Workout> = ArrayList()
        (1..5).forEach {
            val workout = workoutFactory.createWorkout(
                idWorkout = UUID.randomUUID().toString(),
                name = it.toString()+"searched name",
                exercises = null,
                isActive = true,
                created_at = null
            )
            listOfWorkout.add(workout)
            workoutDaoService.insertWorkout(workout)
        }

        val sortedListOfWorkouts = listOfWorkout.sortedByDescending { it.name }

        val searchWorkouts = workoutDaoService.getWorkoutsOrderByNameDESC("",1)
        assertTrue(sortedListOfWorkouts.zip(searchWorkouts).all { (x, y) -> x == y })
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
    private suspend fun insertTestData() : Workout{

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
        val workout = createWorkout()
        val exercise = createExercise()
        val sets = createSets()

        exercise.bodyPart = bodyPart
        exercise.sets = sets
        workout.exercises = listOf(exercise)

        workoutDaoService.insertWorkout(workout)
        exerciseDaoService.insertExercise(exercise)
        sets?.forEach {
            exerciseSetDaoService.insertExerciseSet(it,exercise.idExercise)
        }

        val dateUpdatedAt = dateUtil.getCurrentTimestamp()

        workoutExerciseDaoService.addExerciseToWorkout(workout.idWorkout,exercise.idExercise)
        workoutDaoService.updateExerciseIdsUpdatedAt(workout.idWorkout,dateUpdatedAt)

        workout.exerciseIdsUpdatedAt = dateUpdatedAt

        return workout
    }*/
    override fun injectTest() {
        TODO("Not yet implemented")
    }
}