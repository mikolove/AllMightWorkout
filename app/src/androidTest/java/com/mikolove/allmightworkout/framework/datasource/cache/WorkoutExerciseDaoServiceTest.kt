package com.mikolove.allmightworkout.framework.datasource.cache

import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.di.ProductionModule
import com.mikolove.allmightworkout.framework.BaseTest
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.ExerciseDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.WorkoutDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.WorkoutExerciseDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.database.WorkoutExerciseDao
import com.mikolove.allmightworkout.framework.datasource.cache.implementation.WorkoutDaoServiceImpl
import com.mikolove.allmightworkout.framework.datasource.cache.implementation.WorkoutExerciseDaoServiceImpl
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.test.assertEquals

/*
1. a_isExerciseInWorkout_CBS
2. b_addExerciseToWorkout_CBS
3. removeExerciseFromWorkout
 */
@UninstallModules(ProductionModule::class)
@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class WorkoutExerciseDaoServiceTest : BaseTest(){

    //System in test
    private lateinit var workoutExerciseDaoService : WorkoutExerciseDaoService

    @Inject
    lateinit var workoutExerciseDao : WorkoutExerciseDao

    @Inject
    lateinit var workoutDaoService : WorkoutDaoService

    @Inject
    lateinit var exerciseDaoService : ExerciseDaoService

    @Inject
    lateinit var workoutFactory : WorkoutFactory

    @Inject
    lateinit var exerciseFactory : ExerciseFactory

    @Before
    fun init(){
        injectTest()

        workoutExerciseDaoService = WorkoutExerciseDaoServiceImpl(
            workoutExerciseDao
        )
    }
    override fun injectTest() {
        hiltRule.inject()
    }

    @Test
    fun a_isExerciseInWorkout_CBS() = runBlocking {

        val workout = createWorkout()
        val exercise = createExercise()

        workoutDaoService.insertWorkout(workout)
        exerciseDaoService.insertExercise(exercise)

        workoutExerciseDaoService.addExerciseToWorkout(workout.idWorkout,exercise.idExercise)

        val isExerciseInWorkout = workoutExerciseDao.isExerciseInWorkout(workout.idWorkout,exercise.idExercise)

        assertEquals(isExerciseInWorkout,1)

    }

    @Test
    fun b_addExerciseToWorkout_CBS() = runBlocking {

        val workout = createWorkout()
        val exercise = createExercise()

        workoutDaoService.insertWorkout(workout)
        exerciseDaoService.insertExercise(exercise)

        val isInserted = workoutExerciseDaoService.addExerciseToWorkout(workout.idWorkout,exercise.idExercise)

        assertEquals(isInserted,1)

    }

    @Test
    fun c_removeExerciseFromWorkout() = runBlocking {

        val workout = createWorkout()
        val exercise = createExercise()

        workoutDaoService.insertWorkout(workout)
        exerciseDaoService.insertExercise(exercise)

        val isInserted = workoutExerciseDaoService.addExerciseToWorkout(workout.idWorkout,exercise.idExercise)

        assertEquals(isInserted,1)

        val isDeleted = workoutExerciseDaoService.removeExerciseFromWorkout(workout.idWorkout,exercise.idExercise)

        assertEquals(isDeleted,1)
    }


    private fun createWorkout() : Workout {
        val workout = workoutFactory.createWorkout(
            idWorkout = UUID.randomUUID().toString(),
            name = null,
            exercises = null,
            isActive = true,
            created_at = null
        )
        return workout
    }

    private fun createExercise() : Exercise {

        val exercise = exerciseFactory.createExercise(
            idExercise = UUID.randomUUID().toString(),
            name = null,
            bodyPart = null,
            sets = null,
            exerciseType = ExerciseType.REP_EXERCISE,
            isActive = true,
            created_at = null)

        return exercise
    }
}