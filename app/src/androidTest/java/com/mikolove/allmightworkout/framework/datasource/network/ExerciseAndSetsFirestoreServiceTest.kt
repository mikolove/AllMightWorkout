package com.mikolove.allmightworkout.framework.datasource.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.di.ProductionModule
import com.mikolove.allmightworkout.framework.BaseTest
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.ExerciseFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.ExerciseSetFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.WorkoutFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.implementation.ExerciseFirestoreServiceImpl
import com.mikolove.allmightworkout.framework.datasource.network.implementation.ExerciseSetFirestoreServiceImpl
import com.mikolove.allmightworkout.framework.datasource.network.implementation.WorkoutFirestoreServiceImpl
import com.mikolove.allmightworkout.framework.datasource.network.mappers.ExerciseNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.mappers.ExerciseSetNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.mappers.WorkoutNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreAuth
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/*
LEGEND:
CBS = "Confirm by searching"
Test cases:
1. a_insertExercise_CBS
2. b_insertExerciseAndSets_CBS
3. c_updateExercise_CBS
3. d_updateExerciseSet_CBS
3. e_removeExercise_CBS
4. f_removeExerciseSet_CBS
5. g_insertExerciseSet_CBS
6. h_getExerciseSetsByIdExercise_CBS
7. i_insertDeletedExercise_CBS
8. j_insertDeletedExercises_CBS
9. k_addExerciseToWorkout_CBS
10. l_isExerciseInWorkout_CBS
11. m_removeExerciseFromWorkout_CBS
12. n_getExercises
13. o_insertDeletedExerciseSet_CBS
 */

@UninstallModules(ProductionModule::class)
@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ExerciseAndSetsFirestoreServiceTest : BaseTest() {

    //System in test better to test them together and individually
    private lateinit var exerciseFirestoreService : ExerciseFirestoreService

    private lateinit var exerciseSetFirestoreService : ExerciseSetFirestoreService

    //Init hilt and dependencies
    @Inject
    lateinit var firestore : FirebaseFirestore

    @Inject
    lateinit var firebaseAuth : FirebaseAuth

    //Dependencies
    private lateinit var workoutFirestoreService : WorkoutFirestoreService

    @Inject
    lateinit var workoutFactory : WorkoutFactory

    @Inject
    lateinit var exerciseFactory : ExerciseFactory

    @Inject
    lateinit var exerciseSetFactory: ExerciseSetFactory

    @Inject
    lateinit var bodyPartFactory : BodyPartFactory

    @Inject
    lateinit var workoutNetworkMapper : WorkoutNetworkMapper

    @Inject
    lateinit var exerciseNetworkMapper : ExerciseNetworkMapper

    @Inject
    lateinit var exerciseSetNetworkMapper : ExerciseSetNetworkMapper

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
    fun setup(){

        injectTest()
        //signIn()

        exerciseFirestoreService = ExerciseFirestoreServiceImpl(
            firebaseAuth = firebaseAuth,
            firestore = firestore,
            exerciseNetworkMapper = exerciseNetworkMapper,
            exerciseSetNetworkMapper = exerciseSetNetworkMapper
        )

        exerciseSetFirestoreService = ExerciseSetFirestoreServiceImpl(
            firebaseAuth = firebaseAuth,
            firestore = firestore,
            exerciseNetworkMapper = exerciseNetworkMapper,
            exerciseSetNetworkMapper = exerciseSetNetworkMapper
        )

        workoutFirestoreService = WorkoutFirestoreServiceImpl(
            firebaseAuth = firebaseAuth,
            firestore = firestore,
            workoutNetworkMapper = workoutNetworkMapper,
            exerciseNetworkMapper = exerciseNetworkMapper,
            dateUtil = dateUtil
        )
    }

    @Test
    fun a_insertExercise_CBS() = runBlocking {

        val exercise = createExercise()

        exerciseFirestoreService.insertExercise(exercise)

        val searchResult = exerciseFirestoreService.getExerciseById(exercise.idExercise)

        //Should get exercise without set
        assertEquals(searchResult,exercise)
    }

    @Test
    fun b_insertExerciseAndSets_CBS() = runBlocking {

        val exercise = createExercise()
        exercise.sets = createSets()

        //Insert exercise
        exerciseFirestoreService.insertExercise(exercise)

        //Insert sets
        exercise.sets.forEach { exerciseSet ->
            exerciseSetFirestoreService.insertExerciseSet(exerciseSet,exercise.idExercise)
        }

        val searchResult = exerciseFirestoreService.getExerciseById(exercise.idExercise)

        //Should get exercises with sets
        assertEquals(searchResult?.name,exercise?.name)
        assertEquals(searchResult?.idExercise,exercise?.idExercise)
        assertEquals(searchResult?.isActive,exercise?.isActive)
        assertEquals(searchResult?.createdAt,exercise?.createdAt)
        assertEquals(searchResult?.updatedAt,exercise?.updatedAt)
        assertEquals(searchResult?.bodyPart,exercise?.bodyPart)
        assertTrue { searchResult?.sets?.containsAll(exercise.sets) == true }
    }

    @Test
    fun c_updateExercise_CBS() = runBlocking {

        val exercises = exerciseFirestoreService.getExercises()

        if(exercises.isEmpty())
            throw Exception("No exercises in firestore")

        val exercise = exercises.get(0)

        val bodyPart = bodyPartFactory.createBodyPart(
            idBodyPart = "abs_upper",
            name = "upper abs"
        )
        val updatedExercise = exerciseFactory.createExercise(
            idExercise = exercise.idExercise,
            name = "Name updated",
            sets = exercise.sets,
            bodyPart = bodyPart,
            exerciseType = ExerciseType.TIME_EXERCISE,
            isActive = false,
            created_at = exercise.createdAt
        )

        exerciseFirestoreService.updateExercise(updatedExercise)

        val searchResult = exerciseFirestoreService.getExerciseById(exercise.idExercise)

        assertEquals(searchResult?.idExercise,updatedExercise?.idExercise)
        assertEquals(searchResult?.name,updatedExercise?.name)
        assertEquals(searchResult?.bodyPart,updatedExercise?.bodyPart)
        assertEquals(searchResult?.isActive,updatedExercise?.isActive)
        assertEquals(searchResult?.createdAt,updatedExercise?.createdAt)
        assertEquals(searchResult?.updatedAt,updatedExercise?.updatedAt)
    }

    @Test
    fun d_updateExerciseSet_CBS() = runBlocking {

        val exercises = exerciseFirestoreService.getExercises()

        printLogD("",exercises.toString())
        if(exercises.isEmpty())
            throw Exception("No exercises in firestore")

        val exerciseWithSet = exercises.first {
            it.sets.size > 0
        }

        if(exerciseWithSet == null)
            throw Exception("No exercises with sets in firestore")

        val exerciseSet = exerciseWithSet.sets[0]

        val updatedExerciseSet = exerciseSetFactory.createExerciseSet(
            idExerciseSet = exerciseSet.idExerciseSet,
            reps = 12,
            weight = 12,
            time = 12,
            restTime = 12,
            created_at = exerciseSet.createdAt
        )

        exerciseSetFirestoreService.updateExerciseSet(updatedExerciseSet,exerciseWithSet.idExercise)

        val searchResult = exerciseSetFirestoreService.getExerciseSetById(
            updatedExerciseSet.idExerciseSet,
            exerciseWithSet.idExercise
        )

        assertEquals(searchResult,updatedExerciseSet)
    }

    @Test
    fun e_removeExercise_CBS() = runBlocking {

        //Create a workout
        val exercise = createExercise()
        exercise.sets = createSets()

        //Insert it
        exerciseFirestoreService.insertExercise(exercise)

        //Get it
        val searchInsertExercise = exerciseFirestoreService.getExerciseById(exercise.idExercise)

        assertTrue { searchInsertExercise != null }

        //Remove it
        exerciseFirestoreService.removeExerciseById(exercise.idExercise)

        //Get it again
        val searchRemovedExercise = exerciseFirestoreService.getExerciseById(exercise.idExercise)

        //Assert dont exist
        assertTrue { searchRemovedExercise == null }
    }

    @Test
    fun f_removeExerciseSet_CBS() = runBlocking {

        //Create a workout
        val exercise = createExercise()
        exercise.sets = createSets()

        //Insert it
        exerciseFirestoreService.insertExercise(exercise)

        //Get total set from exercise
        val totalSetsBeforeDelete = exerciseSetFirestoreService.getTotalExercisesSetByExercise(exercise.idExercise)

        assertTrue { totalSetsBeforeDelete != 0 }

        //Remove a random set from it
        exerciseSetFirestoreService.removeExerciseSetById(exercise.sets[0].idExerciseSet,exercise.idExercise)

        //Get total again
        val totalSetsAfterDelete = exerciseSetFirestoreService.getTotalExercisesSetByExercise(exercise.idExercise)

        //Assert deletion
        assertTrue { totalSetsBeforeDelete > totalSetsAfterDelete }

    }

    @Test
    fun g_insertExerciseSet_CBS() = runBlocking {

        val exercises = exerciseFirestoreService.getExercises()

        if(exercises.isEmpty())
            throw Exception("No exercises in firestore")

        val exercise = exercises[0]

        //Create new set
        val set = createSetExercise()

        //Insert it into exercise
        exerciseSetFirestoreService.insertExerciseSet(set,exercise.idExercise)

        val searchExerciseSet = exerciseSetFirestoreService.getExerciseSetById(set.idExerciseSet,exercise.idExercise)

        assertEquals(set,searchExerciseSet)
    }

    @Test
    fun h_getExerciseSetsByIdExercise_CBS() = runBlocking {

        //Create a workout
        val exercise = createExercise()
        exercise.sets = createSets()

        //Insert it
        exerciseFirestoreService.insertExercise(exercise)

        val searchResult = exerciseSetFirestoreService.getExerciseSetByIdExercise(exercise.idExercise)!!


        assertTrue { exercise.sets.containsAll(searchResult) }
        assertTrue { exercise.sets.size == searchResult.size }

    }

    @Test
    fun i_insertDeletedExercise_CBS() = runBlocking {

        val exercises = exerciseFirestoreService.getExercises()

        if(exercises.isEmpty())
            throw Exception("No exercises in firestore")

        val exercise = exercises[0]

        exerciseFirestoreService.insertDeletedExercise(exercise)

        //CBS
        val searchResult = exerciseFirestoreService.getDeletedExercises()

        assertTrue {  searchResult.contains(exercise) }

    }

    @Test
    fun j_insertDeletedExercises_CBS() = runBlocking {

        val exercises = exerciseFirestoreService.getExercises()

        if(exercises.isEmpty())
            throw Exception("No exercises in firestore")


        exerciseFirestoreService.insertDeletedExercises(exercises)

        //CBS
        val searchResult = exerciseFirestoreService.getDeletedExercises()

        assertTrue {  searchResult.containsAll(exercises) }

    }

    @Test
    fun k_addExerciseToWorkout_CBS() = runBlocking {

        //Create workout
        val workout = createWorkout()

        //Create exercise
        val exercise = createExercise()

        workout.exercises = listOf(exercise)

        //Insert them
        workoutFirestoreService.insertWorkout(workout)
        exerciseFirestoreService.insertExercise(exercise)

        //Add exercise to workout
        exerciseFirestoreService.addExerciseToWorkout(workout.idWorkout,exercise.idExercise)

        //Search it
        val searchWorkout = workoutFirestoreService.getWorkoutById(workout.idWorkout)

        assertTrue { workout.exercises!!.containsAll(searchWorkout?.exercises!!) }

    }

    @Test
    fun l_isExerciseInWorkout_CBS() = runBlocking {

        //Create workout
        val workout = createWorkout()

        //Create exercise
        val exercise = createExercise()

        workout.exercises = listOf(exercise)

        //Insert them
        workoutFirestoreService.insertWorkout(workout)
        exerciseFirestoreService.insertExercise(exercise)

        //Add exercise to workout
        exerciseFirestoreService.addExerciseToWorkout(workout.idWorkout,exercise.idExercise)

        val searchResult = exerciseFirestoreService.isExerciseInWorkout(workout.idWorkout,exercise.idExercise)
        assertTrue { searchResult == 1 }
    }

    @Test
    fun m_removeExerciseFromWorkout_CBS() = runBlocking {

        //Create workout
        val workout = createWorkout()

        //Create exercise
        val exercise = createExercise()

        workout.exercises = listOf(exercise)

        //Insert them
        workoutFirestoreService.insertWorkout(workout)
        exerciseFirestoreService.insertExercise(exercise)

        //Add exercise to workout
        exerciseFirestoreService.addExerciseToWorkout(workout.idWorkout,exercise.idExercise)

        val searchExist = exerciseFirestoreService.isExerciseInWorkout(workout.idWorkout,exercise.idExercise)
        assertTrue { searchExist == 1 }

        //Remove it
        exerciseFirestoreService.removeExerciseFromWorkout(workout.idWorkout,exercise.idExercise)

        val searchRemoved = exerciseFirestoreService.isExerciseInWorkout(workout.idWorkout,exercise.idExercise)
        assertTrue { searchRemoved == 0 }

    }

    @Test
    fun n_getExercises() = runBlocking {

        val exercises = exerciseFirestoreService.getExercises()
        val totalExercises = exerciseFirestoreService.getTotalExercises()

        assertTrue { exercises.size == totalExercises }
    }

    @Test
    fun o_insertDeletedExerciseSet_CBS() = runBlocking {

        //Create a workout
        val exercise = createExercise()
        exercise.sets = createSets()

        //Insert it
        exerciseFirestoreService.insertExercise(exercise)

        //Insert set into deleted

        exerciseSetFirestoreService.insertDeletedExerciseSet(exercise.sets[0])

        //CBS
        val searchResult = exerciseSetFirestoreService.getDeletedExerciseSets()
        
        assertTrue {  searchResult.contains(exercise.sets[0]) }
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
    private fun createSetExercise() : ExerciseSet{
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
    private fun createExercise() : Exercise{

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
}