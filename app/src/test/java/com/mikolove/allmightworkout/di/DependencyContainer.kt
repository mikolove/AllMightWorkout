package com.mikolove.allmightworkout.di

import com.mikolove.allmightworkout.business.data.WorkoutDataFactory
import com.mikolove.allmightworkout.business.data.cache.FakeWorkoutCacheDataSourceImpl
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.network.FakeWorkoutNetworkDataSourceImpl
import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.util.isUnitTest
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class DependencyContainer {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH)
    val dateUtil = DateUtil(dateFormat)

    lateinit var workoutNetworkDataSource: WorkoutNetworkDataSource
    lateinit var workoutCacheDataSource: WorkoutCacheDataSource
    lateinit var workoutFactory: WorkoutFactory
    lateinit var workoutDataFactory: WorkoutDataFactory

    lateinit var exerciseFactory: ExerciseFactory
    lateinit var exerciseSetFactory: ExerciseSetFactory

    lateinit var bodyPartFactory: BodyPartFactory
    lateinit var workoutTypeFactory: WorkoutTypeFactory

    init {
        isUnitTest = true // for Logger.kt
    }

    //lateinit var workoutsData : HashMap<String,Workout>
    //In case classLoader is null style have an empty data set
    private var workoutsData : HashMap<String,Workout> = HashMap()

    fun build() {

        this.javaClass.classLoader?.let { classLoader ->

            workoutDataFactory = WorkoutDataFactory(classLoader)

            //Fake data set
            workoutsData = workoutDataFactory.produceHashMapOfWorkouts(
                workoutDataFactory.produceListOfWorkouts()
            )
        }

        workoutTypeFactory = WorkoutTypeFactory()
        bodyPartFactory = BodyPartFactory(workoutTypeFactory)

        exerciseSetFactory = ExerciseSetFactory(dateUtil)
        exerciseFactory = ExerciseFactory(dateUtil,exerciseSetFactory,bodyPartFactory)

        workoutFactory = WorkoutFactory(dateUtil,exerciseFactory)


        workoutNetworkDataSource = FakeWorkoutNetworkDataSourceImpl(
            workoutsData = workoutsData
        )
        workoutCacheDataSource = FakeWorkoutCacheDataSourceImpl(
            workoutsData = workoutsData,
            dateUtil = dateUtil
        )
    }

}